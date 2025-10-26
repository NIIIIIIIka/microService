package com.zjsu.djy.course.repository;

import com.zjsu.djy.course.model.Student;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 学生数据访问：基于ConcurrentHashMap的内存存储，遵循文档1-26至1-44学生管理API
 */
@Repository
public class StudentRepository {
    // 线程安全的Map存储学生（key=学生ID，文档1-94内存存储）
    private final Map<String, Student> students = new ConcurrentHashMap<>();
    // 存储学号与学生ID的映射（用于学号唯一性校验，文档1-31）
    private final Map<String, String> studentIdMap = new ConcurrentHashMap<>();

    /**
     * 查询所有学生：对应文档1-32 GET /api/students
     */
    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }

    /**
     * 根据ID查询学生：对应文档1-35 GET /api/students/{id}
     */
    public Optional<Student> findById(String id) {
        return Optional.ofNullable(students.get(id));
    }

    /**
     * 根据学号查询学生ID：用于学号唯一性校验（文档1-31）
     */
    public Optional<String> findIdByStudentId(String studentId) {
        return Optional.ofNullable(studentIdMap.get(studentId));
    }

    /**
     * 保存学生（创建/更新）：创建时生成UUID和createdAt（文档1-28/1-31）
     */
    public Student save(Student student) {
        if (student.getId() == null) {
            // 创建学生：生成UUID（文档1-46）、设置创建时间戳（文档1-52）
            student.setId(UUID.randomUUID().toString());
            student.setCreatedAt(LocalDateTime.now());
            // 记录学号与ID的映射（用于唯一性校验）
            studentIdMap.put(student.getStudentId(), student.getId());
        } else {
            // 更新学生：若学号变更，更新映射（文档1-41）
            studentIdMap.put(student.getStudentId(), student.getId());
        }
        students.put(student.getId(), student);
        return student;
    }

    /**
     * 根据ID删除学生：对应文档1-42 DELETE /api/students/{id}
     */
    public void deleteById(String id) {
        Student student = students.get(id);
        if (student != null) {
            students.remove(id);
            studentIdMap.remove(student.getStudentId()); // 同步删除学号映射
        }
    }
}