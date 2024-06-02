package com.capgemini.wsb.fitnesstracker.training.internal;

import java.time.LocalDateTime;

public class TrainingDto {
    private Long id;
    private String activity;
    private double distance;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long userId;

    public TrainingDto() {
    }

    public TrainingDto(Long id, String activity, double distance, LocalDateTime startTime, LocalDateTime endTime, Long userId) {
        this.id = id;
        this.activity = activity;
        this.distance = distance;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;
    }

    // Gettery i Settery
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
