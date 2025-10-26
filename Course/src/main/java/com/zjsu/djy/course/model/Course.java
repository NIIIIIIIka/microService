package com.zjsu.djy.course.model;



/**
 * 课程实体
 */

public class Course {
    private String id;                // 课程ID（系统生成UUID）
    private String code;              // 课程编码（如CS101）
    private String title;             // 课程名称
    private Instructor instructor;    // 讲师信息
    private ScheduleSlot schedule;    // 课程安排
    private Integer capacity;         // 课程容量
    private Integer enrolled;         // 已选人数（默认0）

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public ScheduleSlot getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleSlot schedule) {
        this.schedule = schedule;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(Integer enrolled) {
        this.enrolled = enrolled;
    }

    // 初始化已选人数为0

    public Course(String id, String code, String title, Instructor instructor, ScheduleSlot schedule, Integer capacity) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.instructor = instructor;
        this.schedule = schedule;
        this.capacity = capacity;
        this.enrolled = 0;
    }
}