package com.skillswap.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing in-app student notifications.
 */
public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;

    private int notificationId;
    private int userId;
    private String message;
    private String type; // 'GENERAL', 'REQUEST', 'SESSION', 'WALLET', 'REVIEW'
    private String link;
    private boolean isRead;
    private Timestamp createdAt;

    public Notification() {
        this.type = "GENERAL";
        this.link = "#";
        this.isRead = false;
    }

    public Notification(int notificationId, int userId, String message, String type,
                        String link, boolean isRead, Timestamp createdAt) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.link = link;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public int getNotificationId() { return notificationId; }
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}

