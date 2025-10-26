package com.zjsu.djy.course.repository;

import com.zjsu.djy.course.model.Enrollment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 选课数据访问：基于ConcurrentHashMap的内存存储，遵循文档1-53至1-65选课管理API
 */
@Repository
public class EnrollmentRepository {
    // 线程安全的Map存储选课记录（key=选课记录ID，文档1-94内存存储）
    private final Map<String, Enrollment> enrollments = new ConcurrentHashMap<>();

    /**
     * 查询所有选课记录：对应文档1-60 GET /api/enrollments
     */
    public List<Enrollment> findAll() {
        return new ArrayList<>(enrollments.values());
    }

    /**
     * 根据ID查询选课记录：对应文档1-59 DELETE /api/enrollments/{id}（先查询是否存在）
     */
    public Optional<Enrollment> findById(String id) {
        return Optional.ofNullable(enrollments.get(id));
    }

    /**
     * 按课程查询选课记录：对应文档1-62 GET /api/enrollments/course/{courseId}
     */
    public List<Enrollment> findByCourseId(String courseId) {
        return enrollments.values().stream()
                .filter(enrollment -> courseId.equals(enrollment.getCourseId()))
                .collect(Collectors.toList());
    }

    /**
     * 按学生查询选课记录：对应文档1-64 GET /api/enrollments/student/{studentId}
     * 同时用于学生删除前的选课记录检查（文档1-44）
     */
    public List<Enrollment> findByStudentId(String studentId) {
        return enrollments.values().stream()
                .filter(enrollment -> studentId.equals(enrollment.getStudentId()))
                .collect(Collectors.toList());
    }

    /**
     * 检查学生是否已选该课程：对应文档1-69重复选课检查
     */
    public boolean existsByCourseIdAndStudentId(String courseId, String studentId) {
        return enrollments.values().stream()
                .anyMatch(enrollment -> courseId.equals(enrollment.getCourseId())
                        && studentId.equals(enrollment.getStudentId()));
    }

    /**
     * 保存选课记录：创建时生成UUID（文档1-55 POST /api/enrollments）
     */
    public Enrollment save(Enrollment enrollment) {
        if (enrollment.getId() == null) {
            enrollment.setId(UUID.randomUUID().toString()); // 系统生成UUID
        }
        enrollments.put(enrollment.getId(), enrollment);
        return enrollment;
    }

    /**
     * 根据ID删除选课记录：对应文档1-59 DELETE /api/enrollments/{id}
     */
    public void deleteById(String id) {
        enrollments.remove(id);
    }
}