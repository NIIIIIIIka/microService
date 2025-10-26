package com.zjsu.djy.course.model;



/**
 * 选课记录实体
 */

public class Enrollment {
    private String id;         // 选课记录ID（系统生成UUID）
    private String courseId;   // 课程ID
    private String studentId;  // 学生ID（系统生成的UUID）
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

}