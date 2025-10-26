package com.zjsu.djy.course.controller;

import com.zjsu.djy.course.common.ApiResponse;
import com.zjsu.djy.course.model.Student;
import com.zjsu.djy.course.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生管理API：极简实现，对应文档1-26至1-44的学生管理接口
 */
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    // 查询所有学生（GET /api/students）
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllStudents() {
        return success(studentService.getAllStudents());
    }

    // 查询单个学生（GET /api/students/{id}）
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getStudentById(@PathVariable String id) {
        return success(studentService.getStudentById(id));
    }

    // 创建学生（POST /api/students）
    @PostMapping
    public ResponseEntity<Map<String, Object>> createStudent(@RequestBody Student student) {
        return success(studentService.createStudent(student), HttpStatus.CREATED);
    }

    // 更新学生（PUT /api/students/{id}）
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateStudent(
            @PathVariable String id,
            @RequestBody Student student
    ) {
        return success(studentService.updateStudent(id, student));
    }

    // 删除学生（DELETE /api/students/{id}）
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        return ApiResponse.success("删除成功！");
    }

    // 复用成功响应工具方法（同CourseController）
    private ResponseEntity<Map<String, Object>> success(Object data) {
        return success(data, HttpStatus.OK);
    }

    private ResponseEntity<Map<String, Object>> success(Object data, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");
        response.put("data", data);
        return new ResponseEntity<>(response, status);
    }
}