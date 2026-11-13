package com.skillswap.controller;

import com.skillswap.model.Skill;
import com.skillswap.model.User;
import com.skillswap.model.UserAvailability;
import com.skillswap.model.UserSkill;
import com.skillswap.service.SkillService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for Skill Catalog and Student Skill Management (Module 3).
 */
@WebServlet(name = "SkillServlet", urlPatterns = {"/skills"})
public class SkillServlet extends HttpServlet {

    private final SkillService skillService = new SkillService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String categoryFilter = req.getParameter("category");
        String searchQuery = req.getParameter("q");

        List<Skill> allSkills = skillService.getAllSkills();
        List<String> categories = skillService.getAllCategories();

        // Apply filters
        List<Skill> filteredSkills = allSkills.stream()
            .filter(s -> categoryFilter == null || categoryFilter.isEmpty() || s.getCategory().equalsIgnoreCase(categoryFilter))
            .filter(s -> searchQuery == null || searchQuery.isEmpty() ||
                         s.getSkillName().toLowerCase().contains(searchQuery.toLowerCase().trim()) ||
                         s.getDescription().toLowerCase().contains(searchQuery.toLowerCase().trim()))
            .collect(Collectors.toList());

        List<UserSkill> myOffered = skillService.getUserOfferedSkills(currentUser.getUserId());
        List<UserSkill> myWanted = skillService.getUserWantedSkills(currentUser.getUserId());
        List<UserAvailability> myAvailability = skillService.getUserAvailability(currentUser.getUserId());

        req.setAttribute("skills", filteredSkills);
        req.setAttribute("allCatalogSkills", allSkills);
        req.setAttribute("categories", categories);
        req.setAttribute("selectedCategory", categoryFilter);
        req.setAttribute("searchQuery", searchQuery);
        req.setAttribute("myOffered", myOffered);
        req.setAttribute("myWanted", myWanted);
        req.setAttribute("myAvailability", myAvailability);

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

        req.getRequestDispatcher("/skills.jsp").forward(req, resp);
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

        if ("addSkill".equals(action)) {
            String skillIdStr = req.getParameter("skillId");
            String skillType = req.getParameter("skillType"); // 'OFFER' or 'WANT'
            String proficiency = req.getParameter("proficiency");

            try {
                int skillId = Integer.parseInt(skillIdStr);
                boolean ok = skillService.addUserSkill(currentUser.getUserId(), skillId, skillType, proficiency);
                if (ok) {
                    session.setAttribute("flashSuccess", "Skill added to your profile successfully!");
                } else {
                    session.setAttribute("errorMessage", "Failed to add skill. Please ensure all selections are valid.");
                }
            } catch (Exception e) {
                session.setAttribute("errorMessage", "Invalid skill selection.");
            }
            resp.sendRedirect(req.getContextPath() + "/skills");

        } else if ("removeSkill".equals(action)) {
            String userSkillIdStr = req.getParameter("userSkillId");
            try {
                int userSkillId = Integer.parseInt(userSkillIdStr);
                boolean ok = skillService.removeUserSkill(userSkillId, currentUser.getUserId());
                if (ok) {
                    session.setAttribute("flashSuccess", "Skill removed from your profile.");
                } else {
                    session.setAttribute("errorMessage", "Failed to remove skill.");
                }
            } catch (Exception e) {
                session.setAttribute("errorMessage", "Invalid request.");
            }
            resp.sendRedirect(req.getContextPath() + "/skills");

        } else if ("addAvailability".equals(action)) {
            String dayOfWeek = req.getParameter("dayOfWeek");
            String startTimeStr = req.getParameter("startTime");
            String endTimeStr = req.getParameter("endTime");

            try {
                Time startTime = Time.valueOf(startTimeStr + ":00");
                Time endTime = Time.valueOf(endTimeStr + ":00");
                boolean ok = skillService.addAvailabilitySlot(currentUser.getUserId(), dayOfWeek, startTime, endTime);
                if (ok) {
                    session.setAttribute("flashSuccess", "Schedule availability slot added!");
                } else {
                    session.setAttribute("errorMessage", "Failed to add slot. End time must be later than start time.");
                }
            } catch (Exception e) {
                session.setAttribute("errorMessage", "Invalid time format. Please provide valid hours and minutes.");
            }
            resp.sendRedirect(req.getContextPath() + "/skills");

        } else if ("removeAvailability".equals(action)) {
            String availIdStr = req.getParameter("availabilityId");
            try {
                int availId = Integer.parseInt(availIdStr);
                boolean ok = skillService.removeAvailabilitySlot(availId, currentUser.getUserId());
                if (ok) {
                    session.setAttribute("flashSuccess", "Schedule slot removed.");
                } else {
                    session.setAttribute("errorMessage", "Failed to remove schedule slot.");
                }
            } catch (Exception e) {
                session.setAttribute("errorMessage", "Invalid request.");
            }
            resp.sendRedirect(req.getContextPath() + "/skills");
        }
    }
}

