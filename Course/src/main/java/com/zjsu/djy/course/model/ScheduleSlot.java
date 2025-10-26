package com.zjsu.djy.course.model;


public class ScheduleSlot {
    private String dayOfWeek;        // 星期（MONDAY/TUESDAY等）
    private String startTime;        // 开始时间（格式：HH:mm）
    private String endTime;          // 结束时间（格式：HH:mm）
    private Integer expectedAttendance; // 预计出勤人数

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getExpectedAttendance() {
        return expectedAttendance;
    }

    public void setExpectedAttendance(Integer expectedAttendance) {
        this.expectedAttendance = expectedAttendance;
    }
}