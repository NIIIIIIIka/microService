package com.zjsu.djy.course.service;

import com.zjsu.djy.course.exception.BusinessException;
import com.zjsu.djy.course.exception.ResourceNotFoundException;
import com.zjsu.djy.course.model.Student;
import com.zjsu.djy.course.repository.EnrollmentRepository;
import com.zjsu.djy.course.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 学生业务逻辑：实现文档1-26至1-44学生管理API，及1-44删除校验规则
 */
@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository; // 用于删除前选课记录检查（文档1-44）

    /**
     * 查询所有学生：对应文档1-32 GET /api/students
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * 根据ID查询学生：对应文档1-35 GET /api/students/{id}，不存在则抛404（文档1-92）
     */
    public Student getStudentById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    /**
     * 创建学生：对应文档1-28 POST /api/students，校验学号唯一性（文档1-31）
     */
    public Student createStudent(Student student) {
        // 校验邮箱格式
        String email = student.getEmail();
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (email == null || !Pattern.matches(emailRegex, email)) {
            throw new BusinessException("Invalid email format: " + email);
        }

        // 校验学号是否已存在（文档1-31）
        studentRepository.findIdByStudentId(student.getStudentId())
                .ifPresent(id -> {
                    throw new BusinessException("StudentId already exists: " + student.getStudentId()); // 文档1-131错误
                });

        // 保存学生（Repository自动生成ID和createdAt，文档1-46/1-52）
        return studentRepository.save(student);
    }

    /**
     * 更新学生：对应文档1-39 PUT /api/students/{id}，校验学号唯一性+不可修改ID/createdAt（文档1-41）
     */
    public Student updateStudent(String id, Student student) {
        // 1. 校验学生存在（文档1-92）
        Student existingStudent = getStudentById(id);
        // 2. 锁定系统生成字段（不可修改，文档1-41）
        student.setId(id);
        student.setCreatedAt(existingStudent.getCreatedAt());
        // 3. 校验学号唯一性（若学号变更，文档1-41）
        if (!existingStudent.getStudentId().equals(student.getStudentId())) {
            studentRepository.findIdByStudentId(student.getStudentId())
                    .ifPresent(existingId -> {
                        if (!existingId.equals(id)) {
                            throw new BusinessException("StudentId already exists: " + student.getStudentId());
                        }
                    });
        }
        // 4. 更新学生
        return studentRepository.save(student);
    }

    /**
     * 删除学生：对应文档1-42 DELETE /api/students/{id}，校验是否有选课记录（文档1-44）
     */
    public void deleteStudent(String id) {
        // 1. 校验学生存在（文档1-92）
        getStudentById(id);
        // 2. 校验是否有选课记录（文档1-44）
        if (!enrollmentRepository.findByStudentId(id).isEmpty()) {
            throw new BusinessException("无法删除:该学生存在选课记录"); // 文档1-44示例消息
        }
        // 3. 删除学生
        studentRepository.deleteById(id);
    }
}