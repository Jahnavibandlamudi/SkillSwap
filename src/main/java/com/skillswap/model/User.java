package com.skillswap.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing a Student or Administrator at SRM UNIVERSITY AP.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userId;
    private String studentId;     // SRM AP format: AP[0-9]{11}
    private String fullName;
    private String email;
    private String passwordHash;
    private String college;
    private String department;
    private int yearOfStudy;
    private String bio;
    private String profileImage;
    private String role;          // 'STUDENT', 'ADMIN'
    private String status;        // 'ACTIVE', 'INACTIVE'
    private Timestamp createdAt;

    // Transient attributes for UI presentation
    private int creditBalance;
    private double averageRating;
    private int completedSessionsCount;

    public User() {
        this.college = "SRM UNIVERSITY AP";
        this.role = "STUDENT";
        this.status = "ACTIVE";
        this.profileImage = "default-avatar.png";
    }

    public User(int userId, String studentId, String fullName, String email, String passwordHash,
                String college, String department, int yearOfStudy, String bio, String profileImage,
                String role, String status, Timestamp createdAt) {
        this.userId = userId;
        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.college = college;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
        this.bio = bio;
        this.profileImage = profileImage;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public int getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public int getCreditBalance() { return creditBalance; }
    public void setCreditBalance(int creditBalance) { this.creditBalance = creditBalance; }

    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

    public int getCompletedSessionsCount() { return completedSessionsCount; }
    public void setCompletedSessionsCount(int completedSessionsCount) { this.completedSessionsCount = completedSessionsCount; }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.role);
    }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.status);
    }
}

