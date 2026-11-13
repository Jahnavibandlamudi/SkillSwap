package com.skillswap.controller;

import com.skillswap.model.*;
import com.skillswap.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Student Profile Management (Module 2).
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final SkillService skillService = new SkillService();
    private final ReviewService reviewService = new ReviewService();
    private final SessionService sessionService = new SessionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idParam = req.getParameter("id");
        int targetUserId = currentUser.getUserId();
        boolean isOwnProfile = true;

        if (idParam != null && !idParam.isEmpty()) {
            try {
                int requestedId = Integer.parseInt(idParam);
                if (requestedId != currentUser.getUserId()) {
                    targetUserId = requestedId;
                    isOwnProfile = false;
                }
            } catch (NumberFormatException ignored) {}
        }

        User profileUser = userService.getUserById(targetUserId);
        if (profileUser == null) {
            req.setAttribute("errorMessage", "Student profile not found.");
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        List<UserSkill> offeredSkills = skillService.getUserOfferedSkills(targetUserId);
        List<UserSkill> wantedSkills = skillService.getUserWantedSkills(targetUserId);
        List<UserAvailability> availability = skillService.getUserAvailability(targetUserId);
        List<Review> reviews = reviewService.getUserReviews(targetUserId);
        List<LearningSession> completedSessions = sessionService.getCompletedSessions(targetUserId);

        req.setAttribute("profileUser", profileUser);
        req.setAttribute("isOwnProfile", isOwnProfile);
        req.setAttribute("offeredSkills", offeredSkills);
        req.setAttribute("wantedSkills", wantedSkills);
        req.setAttribute("availability", availability);
        req.setAttribute("reviews", reviews);
        req.setAttribute("completedSessions", completedSessions);

        String flashSuccess = (String) session.getAttribute("flashSuccess");
        if (flashSuccess != null) {
            req.setAttribute("flashSuccess", flashSuccess);
            session.removeAttribute("flashSuccess");
        }

        req.getRequestDispatcher("/profile.jsp").forward(req, resp);
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

        if ("updateProfile".equals(action)) {
            String fullName = req.getParameter("fullName");
            String department = req.getParameter("department");
            String yearStr = req.getParameter("yearOfStudy");
            String bio = req.getParameter("bio");
            String profileImage = req.getParameter("profileImage");

            int yearOfStudy = currentUser.getYearOfStudy();
            try {
                if (yearStr != null) yearOfStudy = Integer.parseInt(yearStr.trim());
            } catch (NumberFormatException ignored) {}

            boolean ok = userService.updateProfile(currentUser.getUserId(), fullName, department,
                                                  yearOfStudy, bio, profileImage);
            if (ok) {
                User updated = userService.getUserById(currentUser.getUserId());
                session.setAttribute("user", updated);
                session.setAttribute("flashSuccess", "Profile updated successfully!");
            } else {
                session.setAttribute("errorMessage", "Failed to update profile. Please verify your inputs.");
            }
            resp.sendRedirect(req.getContextPath() + "/profile");

        } else if ("changePassword".equals(action)) {
            String currentPassword = req.getParameter("currentPassword");
            String newPassword = req.getParameter("newPassword");
            String confirmPassword = req.getParameter("confirmPassword");

            boolean ok = userService.changePassword(currentUser.getUserId(), currentPassword, newPassword, confirmPassword);
            if (ok) {
                session.setAttribute("flashSuccess", "Password changed successfully!");
            } else {
                session.setAttribute("errorMessage", "Password change failed. Check your current password and ensure new passwords match (min 6 characters).");
            }
            resp.sendRedirect(req.getContextPath() + "/profile");
        }
    }
}

