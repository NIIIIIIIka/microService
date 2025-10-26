package com.zjsu.djy.course.service;

import com.zjsu.djy.course.exception.BusinessException;
import com.zjsu.djy.course.exception.ResourceNotFoundException;
import com.zjsu.djy.course.model.Course;
import com.zjsu.djy.course.model.Enrollment;
import com.zjsu.djy.course.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 选课业务逻辑：实现文档1-53至1-65选课管理API，及1-68至1-73所有业务规则
 */
@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseService courseService;     // 用于课程校验+容量检查+级联更新（文档1-68/1-70/1-72）
    private final StudentService studentService;   // 用于学生校验（文档1-71）

    /**
     * 查询所有选课记录：对应文档1-60 GET /api/enrollments
     */
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    /**
     * 根据ID查询选课记录：对应文档1-59 DELETE /api/enrollments/{id}（先查询）
     */
    public Enrollment getEnrollmentById(String id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
    }

    /**
     * 按课程查询选课记录：对应文档1-62 GET /api/enrollments/course/{courseId}
     */
    public List<Enrollment> getEnrollmentsByCourseId(String courseId) {
        // 校验课程存在（文档1-70）
        courseService.getCourseById(courseId);
        return enrollmentRepository.findByCourseId(courseId);
    }

    /**
     * 按学生查询选课记录：对应文档1-64 GET /api/enrollments/student/{studentId}
     */
    public List<Enrollment> getEnrollmentsByStudentId(String studentId) {
        // 校验学生存在（文档1-71）
        studentService.getStudentById(studentId);
        return enrollmentRepository.findByStudentId(studentId);
    }

    /**
     * 学生选课：对应文档1-55 POST /api/enrollments，实现所有业务规则（文档1-68至1-73）
     */
    public Enrollment enrollCourse(Enrollment enrollment) {
        String courseId = enrollment.getCourseId();
        String studentId = enrollment.getStudentId();

        // 1. 校验学生存在（文档1-71）
        studentService.getStudentById(studentId);

        // 2. 校验课程存在（文档1-70）
        Course course = courseService.getCourseById(courseId);

        // 3. 校验重复选课（文档1-69）
        if (enrollmentRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
            throw new BusinessException("无法选课:学生已选该课程");
        }

        // 4. 校验课程容量（文档1-68）
        if (course.getEnrolled() >= course.getCapacity()) {
            throw new BusinessException("无法选课:课程容量已满");
        }

        // 5. 保存选课记录（Repository自动生成ID）
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // 6. 级联更新：课程已选人数+1（文档1-72）
        courseService.incrementEnrolledCount(courseId);

        return savedEnrollment;
    }

    /**
     * 学生退课：对应文档1-59 DELETE /api/enrollments/{id}，级联更新课程人数（文档1-72）
     */
    public void dropCourse(String enrollmentId) {
        // 1. 校验选课记录存在
        Enrollment enrollment = getEnrollmentById(enrollmentId);
        String courseId = enrollment.getCourseId();

        // 2. 删除选课记录
        enrollmentRepository.deleteById(enrollmentId);

        // 3. 级联更新：课程已选人数-1（文档1-72）
        courseService.decrementEnrolledCount(courseId);
    }
}