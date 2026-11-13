package com.skillswap.dao;

import com.skillswap.model.Notification;
import com.skillswap.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for in-app Notifications.
 */
public class NotificationDAO {

    private static final Logger LOGGER = Logger.getLogger(NotificationDAO.class.getName());

    public int createNotification(Notification notif) {
        String sql = "INSERT INTO notifications (user_id, message, type, link, is_read) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, notif.getUserId());
            ps.setString(2, notif.getMessage());
            ps.setString(3, notif.getType() != null ? notif.getType() : "GENERAL");
            ps.setString(4, notif.getLink() != null ? notif.getLink() : "#");
            ps.setBoolean(5, notif.isRead());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating notification", e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return -1;
    }

    public List<Notification> findByUserId(int userId, int limit) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC LIMIT ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, limit > 0 ? limit : 20);
            rs = ps.executeQuery();
            while (rs.next()) {
                Notification n = new Notification(
                    rs.getInt("notification_id"),
                    rs.getInt("user_id"),
                    rs.getString("message"),
                    rs.getString("type"),
                    rs.getString("link"),
                    rs.getBoolean("is_read"),
                    rs.getTimestamp("created_at")
                );
                list.add(n);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching notifications for user: " + userId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    public int getUnreadCount(int userId) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = FALSE";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting unread notifications for user: " + userId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return 0;
    }

    public boolean markAsRead(int notificationId, int userId) {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE notification_id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, notificationId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error marking notification as read: " + notificationId, e);
        } finally {
            DBConnection.closeQuietly(null, ps, conn);
        }
        return false;
    }

    public boolean markAllAsRead(int userId) {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE user_id = ? AND is_read = FALSE";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error marking all notifications as read for user: " + userId, e);
        } finally {
            DBConnection.closeQuietly(null, ps, conn);
        }
        return false;
    }
}

