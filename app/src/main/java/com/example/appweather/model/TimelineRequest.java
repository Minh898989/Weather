package com.example.appweather.model;

import androidx.annotation.Nullable;

import java.util.List;

public class TimelineRequest {

    private String location;
    private List<String> fields;
    private List<String> timesteps;
    private String units;
    private String startTime;
    private String endTime;

    @Nullable
    private Integer dailyStartHour;

    /**
     * Constructor để dễ dàng tạo một đối tượng request hoàn chỉnh.
     * 
     * @param location  Tọa độ hoặc tên thành phố.
     * @param fields    Danh sách các trường dữ liệu muốn lấy (e.g., "temperature").
     * @param timesteps Danh sách các bước thời gian (e.g., "1h", "1d").
     * @param units     Hệ đơn vị ("metric" hoặc "imperial").
     * @param startTime Thời gian bắt đầu.
     * @param endTime   Thời gian kết thúc.
     */
    public TimelineRequest(String location, List<String> fields, List<String> timesteps, String units, String startTime,
            String endTime, Integer dailyStartHour) {
        this.location = location;
        this.fields = fields;
        this.timesteps = timesteps;
        this.units = units;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dailyStartHour = dailyStartHour;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<String> getTimesteps() {
        return timesteps;
    }

    public void setTimesteps(List<String> timesteps) {
        this.timesteps = timesteps;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
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

    public Integer getDailyStartHour() {
        return dailyStartHour;
    }

    public void setDailyStartHour(Integer dailyStartHour) {
        this.dailyStartHour = dailyStartHour;
    }
}
