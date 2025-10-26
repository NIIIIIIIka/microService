package com.zjsu.djy.course.service;

import com.zjsu.djy.course.exception.ResourceNotFoundException;
import com.zjsu.djy.course.model.Course;
import com.zjsu.djy.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 课程业务逻辑：实现文档1-12至1-25课程管理API，及1-72级联更新规则
 */
@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    /**
     * 查询所有课程：对应文档1-14 GET /api/courses
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * 根据ID查询课程：对应文档1-16 GET /api/courses/{id}，不存在则抛404（文档1-92）
     */
    public Course getCourseById(String id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id)); // 文档1-84示例消息
    }

    /**
     * 创建课程：对应文档1-18 POST /api/courses，自动生成ID（文档1-46）
     */
    public Course createCourse(Course course) {
        if(course.getEnrolled()==null)
            course.setEnrolled(0);
        return courseRepository.save(course); // Repository中自动生成UUID
    }

    /**
     * 更新课程：对应文档1-22 PUT /api/courses/{id}，不可修改ID（文档1-46）
     */
    public Course updateCourse(String id, Course course) {
        // 1. 校验课程存在（文档1-92）
        getCourseById(id);
        // 2. 锁定ID（不可修改，文档1-46）
        course.setId(id);
        // 3. 更新课程
        return courseRepository.save(course);
    }

    /**
     * 删除课程：对应文档1-24 DELETE /api/courses/{id}
     */
    public void deleteCourse(String id) {
        // 校验课程存在（文档1-92）
        getCourseById(id);
        courseRepository.deleteById(id);
    }

    /**
     * 选课成功后增加已选人数：对应文档1-72级联更新规则
     */
    public void incrementEnrolledCount(String courseId) {
        Course course = getCourseById(courseId);
        course.setEnrolled(course.getEnrolled() + 1);
        courseRepository.save(course);
    }

    /**
     * 退课成功后减少已选人数：对应文档1-72级联更新规则
     */
    public void decrementEnrolledCount(String courseId) {
        Course course = getCourseById(courseId);
        course.setEnrolled(course.getEnrolled() - 1);
        courseRepository.save(course);
    }
}