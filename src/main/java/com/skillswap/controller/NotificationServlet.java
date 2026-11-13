package com.skillswap.controller;

import com.skillswap.model.Notification;
import com.skillswap.model.User;
import com.skillswap.service.NotificationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for In-App Notifications (Module 9).
 */
@WebServlet(name = "NotificationServlet", urlPatterns = {"/notifications"})
public class NotificationServlet extends HttpServlet {

    private final NotificationService notificationService = new NotificationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Notification> notifications = notificationService.getRecentNotifications(currentUser.getUserId(), 50);
        int unreadCount = notificationService.getUnreadCount(currentUser.getUserId());

        req.setAttribute("notifications", notifications);
        req.setAttribute("unreadCount", unreadCount);

        String flashSuccess = (String) session.getAttribute("flashSuccess");
        if (flashSuccess != null) {
            req.setAttribute("flashSuccess", flashSuccess);
            session.removeAttribute("flashSuccess");
        }

        req.getRequestDispatcher("/notifications.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");

        if ("markRead".equals(action)) {
            try {
                int notifId = Integer.parseInt(req.getParameter("id"));
                notificationService.markAsRead(notifId, currentUser.getUserId());
            } catch (Exception ignored) {}
        } else if ("markAllRead".equals(action)) {
            notificationService.markAllAsRead(currentUser.getUserId());
            session.setAttribute("flashSuccess", "All notifications marked as read.");
        }

        resp.sendRedirect(req.getContextPath() + "/notifications");
    }
}

