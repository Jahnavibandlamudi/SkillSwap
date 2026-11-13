package com.skillswap.controller;

import com.skillswap.model.ExchangeRequest;
import com.skillswap.model.LearningSession;
import com.skillswap.model.Skill;
import com.skillswap.model.User;
import com.skillswap.service.AdminService;
import com.skillswap.service.SkillService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller for Administrator Dashboard and System Management (Module 10).
 */
@WebServlet(name = "AdminServlet", urlPatterns = {"/admin", "/admin-dashboard"})
public class AdminServlet extends HttpServlet {

    private final AdminService adminService = new AdminService();
    private final SkillService skillService = new SkillService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null || !currentUser.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Map<String, Integer> stats = adminService.getDashboardStatistics();
        List<User> students = adminService.getAllUsers();
        List<Skill> skills = adminService.getAllSkills();
        List<ExchangeRequest> requests = adminService.getAllRequests();
        List<LearningSession> sessions = adminService.getAllSessions();
        List<String> categories = skillService.getAllCategories();

        req.setAttribute("stats", stats);
        req.setAttribute("students", students);
        req.setAttribute("skills", skills);
        req.setAttribute("requests", requests);
        req.setAttribute("sessions", sessions);
        req.setAttribute("categories", categories);

        String flashSuccess = (String) session.getAttribute("flashSuccess");
        if (flashSuccess != null) {
            req.setAttribute("flashSuccess", flashSuccess);
            session.removeAttribute("flashSuccess");
        }
        String flashError = (String) session.getAttribute("errorMessage");
        if (flashError != null) {
            req.setAttribute("errorMessage", flashError);
            session.removeAttribute("errorMessage");
        }

        req.getRequestDispatcher("/admin-dashboard.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null || !currentUser.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");

        if ("toggleStatus".equals(action)) {
            try {
                int userId = Integer.parseInt(req.getParameter("userId"));
                String newStatus = req.getParameter("newStatus");
                boolean ok = adminService.toggleUserStatus(userId, newStatus);
                if (ok) {
                    session.setAttribute("flashSuccess", "Student account status updated to " + newStatus);
                } else {
                    session.setAttribute("errorMessage", "Failed to update account status.");
                }
            } catch (Exception e) {
                session.setAttribute("errorMessage", "Invalid user ID.");
            }
            resp.sendRedirect(req.getContextPath() + "/admin");

        } else if ("addSkill".equals(action)) {
            String skillName = req.getParameter("skillName");
            String category = req.getParameter("category");
            String description = req.getParameter("description");

            boolean ok = skillService.createSkill(skillName, category, description);
            if (ok) {
                session.setAttribute("flashSuccess", "New skill '" + skillName + "' added to catalog!");
            } else {
                session.setAttribute("errorMessage", "Failed to add skill. Name and category are required.");
            }
            resp.sendRedirect(req.getContextPath() + "/admin");

        } else if ("deleteSkill".equals(action)) {
            try {
                int skillId = Integer.parseInt(req.getParameter("skillId"));
                boolean ok = skillService.deleteSkill(skillId);
                if (ok) {
                    session.setAttribute("flashSuccess", "Skill deleted from catalog.");
                } else {
                    session.setAttribute("errorMessage", "Failed to delete skill.");
                }
            } catch (Exception e) {
                session.setAttribute("errorMessage", "Invalid skill ID.");
            }
            resp.sendRedirect(req.getContextPath() + "/admin");
        }
    }
}

