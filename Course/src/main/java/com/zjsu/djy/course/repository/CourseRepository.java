package com.zjsu.djy.course.repository;

import com.zjsu.djy.course.model.Course;
import com.zjsu.djy.course.model.Instructor;
import com.zjsu.djy.course.model.ScheduleSlot;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 课程数据访问：基于ConcurrentHashMap的内存存储，完全遵循文档1-94/1-97要求
 */
@Repository
public class CourseRepository {
    // 线程安全的Map存储课程（key=课程ID，文档1-97示例）
    private final Map<String, Course> courses = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public CourseRepository() {
    }
    /**
     * 查询所有课程：对应文档1-14 GET /api/courses
     */
    public List<Course> findAll() {

        return new ArrayList<>(courses.values());
    }

    /**
     * 根据ID查询课程：对应文档1-16 GET /api/courses/{id}
     */
    public Optional<Course> findById(String id) {

        return Optional.ofNullable(courses.get(id));
    }

    /**
     * 保存课程（创建/更新）：创建时自动生成UUID（文档1-18 POST /api/courses）
     */
    public Course save(Course course) {
        // 1. 字符串比较应使用equals()而非==，避免引用比较导致的逻辑错误
        // 2. 生成新UUID通常用于新增场景，更新时一般不应修改ID，此处假设需求是"当code匹配时重新生成ID"，保留逻辑但修正比较方式
        // 确保ID不为空再存入（避免空键异常）
        if (course.getId() == null) {
            course.setId(UUID.randomUUID().toString());
        }

        courses.put(course.getId(), course);
        return course;
    }
    public Course createCourse(Course course){
        String id=UUID.randomUUID().toString();
        course.setId(id);
        courses.put(id, course);
        return course;
    }
    /**
     * 根据ID删除课程：对应文档1-24 DELETE /api/courses/{id}
     */
    public void deleteById(String id) {
        courses.remove(id);
    }
}