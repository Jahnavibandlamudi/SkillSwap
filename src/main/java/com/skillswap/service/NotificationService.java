package com.skillswap.service;

import com.skillswap.dao.NotificationDAO;
import com.skillswap.model.Notification;

import java.util.List;

/**
 * Service for Notification management (Module 9).
 */
public class NotificationService {

    private final NotificationDAO notificationDAO = new NotificationDAO();

    public List<Notification> getRecentNotifications(int userId, int limit) {
        return notificationDAO.findByUserId(userId, limit);
    }

    public int getUnreadCount(int userId) {
        return notificationDAO.getUnreadCount(userId);
    }

    public boolean markAsRead(int notificationId, int userId) {
        return notificationDAO.markAsRead(notificationId, userId);
    }

    public boolean markAllAsRead(int userId) {
        return notificationDAO.markAllAsRead(userId);
    }
}

