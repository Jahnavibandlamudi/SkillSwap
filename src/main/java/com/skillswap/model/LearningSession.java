package com.skillswap.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Model representing a scheduled, completed, or cancelled learning session.
 */
public class LearningSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private int sessionId;
    private int requestId;
    private int teacherId;
    private int learnerId;
    private int skillId;
    private Date sessionDate;
    private Time startTime;
    private Time endTime;
    private String meetingMode;     // 'ONLINE' or 'OFFLINE'
    private String meetingDetails;  // Google Meet link or SRM AP Campus Venue
    private String status;          // 'SCHEDULED', 'COMPLETED', 'CANCELLED'
    private boolean teacherConfirmed;
    private boolean learnerConfirmed;
    private Timestamp createdAt;

    // Joined presentation attributes
    private String teacherName;
    private String teacherEmail;
    private String teacherDepartment;
    private String learnerName;
    private String learnerEmail;
    private String learnerDepartment;
    private String skillName;
    private boolean userHasReviewed; // Whether current session participant has reviewed

    public LearningSession() {
        this.status = "SCHEDULED";
        this.meetingMode = "ONLINE";
        this.teacherConfirmed = false;
        this.learnerConfirmed = false;
    }

    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }

    public int getLearnerId() { return learnerId; }
    public void setLearnerId(int learnerId) { this.learnerId = learnerId; }

    public int getSkillId() { return skillId; }
    public void setSkillId(int skillId) { this.skillId = skillId; }

    public Date getSessionDate() { return sessionDate; }
    public void setSessionDate(Date sessionDate) { this.sessionDate = sessionDate; }

    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime) { this.startTime = startTime; }

    public Time getEndTime() { return endTime; }
    public void setEndTime(Time endTime) { this.endTime = endTime; }

    public String getMeetingMode() { return meetingMode; }
    public void setMeetingMode(String meetingMode) { this.meetingMode = meetingMode; }

    public String getMeetingDetails() { return meetingDetails; }
    public void setMeetingDetails(String meetingDetails) { this.meetingDetails = meetingDetails; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isTeacherConfirmed() { return teacherConfirmed; }
    public void setTeacherConfirmed(boolean teacherConfirmed) { this.teacherConfirmed = teacherConfirmed; }

    public boolean isLearnerConfirmed() { return learnerConfirmed; }
    public void setLearnerConfirmed(boolean learnerConfirmed) { this.learnerConfirmed = learnerConfirmed; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public String getTeacherEmail() { return teacherEmail; }
    public void setTeacherEmail(String teacherEmail) { this.teacherEmail = teacherEmail; }

    public String getTeacherDepartment() { return teacherDepartment; }
    public void setTeacherDepartment(String teacherDepartment) { this.teacherDepartment = teacherDepartment; }

    public String getLearnerName() { return learnerName; }
    public void setLearnerName(String learnerName) { this.learnerName = learnerName; }

    public String getLearnerEmail() { return learnerEmail; }
    public void setLearnerEmail(String learnerEmail) { this.learnerEmail = learnerEmail; }

    public String getLearnerDepartment() { return learnerDepartment; }
    public void setLearnerDepartment(String learnerDepartment) { this.learnerDepartment = learnerDepartment; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public boolean isUserHasReviewed() { return userHasReviewed; }
    public void setUserHasReviewed(boolean userHasReviewed) { this.userHasReviewed = userHasReviewed; }

    public boolean isScheduled() { return "SCHEDULED".equalsIgnoreCase(status); }
    public boolean isCompleted() { return "COMPLETED".equalsIgnoreCase(status); }
    public boolean isCancelled() { return "CANCELLED".equalsIgnoreCase(status); }

    public boolean isBothConfirmed() {
        return teacherConfirmed && learnerConfirmed;
    }
}

