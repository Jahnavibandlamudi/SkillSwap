package com.skillswap.model;

import java.io.Serializable;
import java.sql.Time;

/**
 * Model representing student's available learning/teaching schedule slots.
 */
public class UserAvailability implements Serializable {
    private static final long serialVersionUID = 1L;

    private int availabilityId;
    private int userId;
    private String dayOfWeek;  // Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
    private Time startTime;
    private Time endTime;

    public UserAvailability() {}

    public UserAvailability(int availabilityId, int userId, String dayOfWeek, Time startTime, Time endTime) {
        this.availabilityId = availabilityId;
        this.userId = userId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getAvailabilityId() { return availabilityId; }
    public void setAvailabilityId(int availabilityId) { this.availabilityId = availabilityId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime) { this.startTime = startTime; }

    public Time getEndTime() { return endTime; }
    public void setEndTime(Time endTime) { this.endTime = endTime; }

    public String getFormattedTiming() {
        return dayOfWeek + " (" + (startTime != null ? startTime.toString().substring(0, 5) : "") + " - " +
               (endTime != null ? endTime.toString().substring(0, 5) : "") + ")";
    }
}

