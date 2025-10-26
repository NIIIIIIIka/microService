package com.zjsu.djy.course.model;


import java.time.LocalDateTime;


public class Student {
    private String id;          // 系统生成UUID（唯一标识）
    private String studentId;   // 学号（全局唯一）
    private String name;        // 姓名（必填）
    private String major;       // 专业（必填）
    private Integer grade;      // 入学年份（必填）
    private String email;       // 邮箱（必填，格式校验）
    private LocalDateTime createdAt; // 创建时间戳（系统生成）

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}