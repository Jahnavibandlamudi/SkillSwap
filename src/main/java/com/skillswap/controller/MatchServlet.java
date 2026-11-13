package com.skillswap.controller;

import com.skillswap.model.MatchResult;
import com.skillswap.model.User;
import com.skillswap.model.UserSkill;
import com.skillswap.service.MatchService;
import com.skillswap.service.SkillService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Skill Matching and Partner Recommendations (Module 4).
 */
@WebServlet(name = "MatchServlet", urlPatterns = {"/matches"})
public class MatchServlet extends HttpServlet {

    private final MatchService matchService = new MatchService();
    private final SkillService skillService = new SkillService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String category = req.getParameter("category");
        String mutualStr = req.getParameter("mutualOnly");
        boolean mutualOnly = "true".equalsIgnoreCase(mutualStr) || "1".equals(mutualStr);
        String query = req.getParameter("q");

        List<MatchResult> matches = matchService.findMatches(currentUser.getUserId(), category, mutualOnly, query);
        List<String> categories = skillService.getAllCategories();
        List<UserSkill> myOffered = skillService.getUserOfferedSkills(currentUser.getUserId());
        List<UserSkill> myWanted = skillService.getUserWantedSkills(currentUser.getUserId());

        req.setAttribute("matches", matches);
        req.setAttribute("categories", categories);
        req.setAttribute("selectedCategory", category);
        req.setAttribute("mutualOnly", mutualOnly);
        req.setAttribute("searchQuery", query);
        req.setAttribute("myOffered", myOffered);
        req.setAttribute("myWanted", myWanted);

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

        req.getRequestDispatcher("/matches.jsp").forward(req, resp);
    }
}

