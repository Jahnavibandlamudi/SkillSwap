package com.skillswap.controller;

import com.skillswap.model.ExchangeRequest;
import com.skillswap.model.LearningSession;
import com.skillswap.model.User;
import com.skillswap.service.ExchangeService;
import com.skillswap.service.SessionService;
import com.skillswap.service.SessionService.SessionResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * Controller for Learning Session Management & Confirmation (Module 6).
 */
@WebServlet(name = "SessionServlet", urlPatterns = {"/sessions"})
public class SessionServlet extends HttpServlet {

    private final SessionService sessionService = new SessionService();
    private final ExchangeService exchangeService = new ExchangeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int userId = currentUser.getUserId();
        List<LearningSession> allSessions = sessionService.getUserSessions(userId);
        List<LearningSession> upcoming = sessionService.getUpcomingSessions(userId);
        List<LearningSession> completed = sessionService.getCompletedSessions(userId);
        List<ExchangeRequest> acceptedRequests = exchangeService.getAcceptedRequests(userId);

        req.setAttribute("allSessions", allSessions);
        req.setAttribute("upcomingSessions", upcoming);
        req.setAttribute("completedSessions", completed);
        req.setAttribute("acceptedRequests", acceptedRequests);

        // Pre-fill requestId if provided
        String reqIdParam = req.getParameter("requestId");
        if (reqIdParam != null && !reqIdParam.isEmpty()) {
            try {
                int prefillReqId = Integer.parseInt(reqIdParam);
                req.setAttribute("prefillRequestId", prefillReqId);
            } catch (NumberFormatException ignored) {}
        }

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

        req.getRequestDispatcher("/sessions.jsp").forward(req, resp);
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

        if ("schedule".equals(action)) {
            try {
                int requestId = Integer.parseInt(req.getParameter("requestId"));
                int teacherId = Integer.parseInt(req.getParameter("teacherId"));
                int learnerId = Integer.parseInt(req.getParameter("learnerId"));
                int skillId = Integer.parseInt(req.getParameter("skillId"));
                Date sessionDate = Date.valueOf(req.getParameter("sessionDate"));
                Time startTime = Time.valueOf(req.getParameter("startTime") + ":00");
                Time endTime = Time.valueOf(req.getParameter("endTime") + ":00");
                String meetingMode = req.getParameter("meetingMode");
                String meetingDetails = req.getParameter("meetingDetails");

                SessionResult result = sessionService.scheduleSession(
                    requestId, currentUser.getUserId(), teacherId, learnerId, skillId,
                    sessionDate, startTime, endTime, meetingMode, meetingDetails
                );

                if (result.isSuccess()) {
                    session.setAttribute("flashSuccess", result.getMessage());
                } else {
                    session.setAttribute("errorMessage", result.getMessage());
                }
            } catch (Exception e) {
                session.setAttribute("errorMessage", "Error scheduling session: Please check all required fields and date/time formats.");
            }
            resp.sendRedirect(req.getContextPath() + "/sessions");

        } else if ("confirm".equals(action)) {
            try {
                int sessionId = Integer.parseInt(req.getParameter("sessionId"));
                SessionResult result = sessionService.confirmCompletion(sessionId, currentUser.getUserId());
                if (result.isSuccess()) {
                    session.setAttribute("flashSuccess", result.getMessage());
                } else {
                    session.setAttribute("errorMessage", result.getMessage());
                }
            } catch (Exception e) {
                session.setAttribute("errorMessage", "Error confirming session.");
            }
            resp.sendRedirect(req.getContextPath() + "/sessions");

        } else if ("cancel".equals(action)) {
            try {
                int sessionId = Integer.parseInt(req.getParameter("sessionId"));
                boolean ok = sessionService.cancelSession(sessionId, currentUser.getUserId());
                if (ok) {
                    session.setAttribute("flashSuccess", "Session cancelled successfully.");
                } else {
                    session.setAttribute("errorMessage", "Unable to cancel session.");
                }
            } catch (Exception e) {
                session.setAttribute("errorMessage", "Invalid session request.");
            }
            resp.sendRedirect(req.getContextPath() + "/sessions");
        }
    }
}

