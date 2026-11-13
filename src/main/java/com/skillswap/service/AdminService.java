package com.skillswap.service;

import com.skillswap.dao.*;
import com.skillswap.model.ExchangeRequest;
import com.skillswap.model.LearningSession;
import com.skillswap.model.Skill;
import com.skillswap.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for Administrator Operations & Analytics (Module 10).
 */
public class AdminService {

    private final UserDAO userDAO = new UserDAO();
    private final SkillDAO skillDAO = new SkillDAO();
    private final ExchangeDAO exchangeDAO = new ExchangeDAO();
    private final SessionDAO sessionDAO = new SessionDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();

    public Map<String, Integer> getDashboardStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalStudents", userDAO.getStudentCount());
        stats.put("activeStudents", userDAO.getActiveStudentCount());
        stats.put("totalSkills", skillDAO.getTotalSkillsCount());
        stats.put("totalRequests", exchangeDAO.getTotalRequestCount());
        stats.put("acceptedRequests", exchangeDAO.getAcceptedRequestCount());
        stats.put("completedSessions", sessionDAO.getCompletedSessionsCount());
        stats.put("totalReviews", reviewDAO.getTotalReviewCount());
        return stats;
    }

    public List<User> getAllUsers() {
        return userDAO.findAllStudents();
    }

    public boolean toggleUserStatus(int userId, String newStatus) {
        if (!"ACTIVE".equalsIgnoreCase(newStatus) && !"INACTIVE".equalsIgnoreCase(newStatus)) {
            return false;
        }
        return userDAO.updateStatus(userId, newStatus.toUpperCase());
    }

    public List<Skill> getAllSkills() {
        return skillDAO.findAllSkills();
    }

    public List<ExchangeRequest> getAllRequests() {
        return exchangeDAO.findAllRequests();
    }

    public List<LearningSession> getAllSessions() {
        return sessionDAO.findAllSessions();
    }
}

