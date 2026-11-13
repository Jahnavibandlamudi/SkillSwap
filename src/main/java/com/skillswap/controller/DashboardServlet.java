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
 * Controller for Student Dashboard.
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    private final AuthService authService = new AuthService();
    private final UserService userService = new UserService();
    private final SkillService skillService = new SkillService();
    private final MatchService matchService = new MatchService();
    private final ExchangeService exchangeService = new ExchangeService();
    private final SessionService sessionService = new SessionService();
    private final NotificationService notificationService = new NotificationService();
    private final CreditService creditService = new CreditService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Refresh user attributes
        User refreshedUser = userService.getUserById(currentUser.getUserId());
        if (refreshedUser != null) {
            session.setAttribute("user", refreshedUser);
            currentUser = refreshedUser;
        }

        int userId = currentUser.getUserId();

        // 1. Credit wallet balance
        int creditBalance = creditService.getBalance(userId);
        req.setAttribute("creditBalance", creditBalance);

        // 2. Student skills
        List<UserSkill> offeredSkills = skillService.getUserOfferedSkills(userId);
        List<UserSkill> wantedSkills = skillService.getUserWantedSkills(userId);
        req.setAttribute("offeredSkills", offeredSkills);
        req.setAttribute("wantedSkills", wantedSkills);

        // 3. Profile completeness calculation
        int completeness = 40; // Base: registration
        if (offeredSkills.size() > 0) completeness += 20;
        if (wantedSkills.size() > 0) completeness += 20;
        if (currentUser.getBio() != null && !currentUser.getBio().isEmpty()) completeness += 10;
        List<UserAvailability> availability = skillService.getUserAvailability(userId);
        if (availability.size() > 0) completeness += 10;
        req.setAttribute("profileCompleteness", Math.min(100, completeness));

        // 4. Smart recommended matches (top 4)
        List<MatchResult> matches = matchService.findMatches(userId, null, false, null);
        List<MatchResult> topMatches = matches.size() > 4 ? matches.subList(0, 4) : matches;
        req.setAttribute("recommendedMatches", topMatches);

        // 5. Pending incoming requests
        List<ExchangeRequest> receivedRequests = exchangeService.getReceivedRequests(userId);
        long pendingCount = receivedRequests.stream().filter(ExchangeRequest::isPending).count();
        req.setAttribute("pendingRequestsCount", pendingCount);
        req.setAttribute("receivedRequests", receivedRequests);

        // 6. Upcoming sessions
        List<LearningSession> upcomingSessions = sessionService.getUpcomingSessions(userId);
        req.setAttribute("upcomingSessions", upcomingSessions);

        // 7. Recent notifications
        List<Notification> recentNotifications = notificationService.getRecentNotifications(userId, 5);
        int unreadNotifs = notificationService.getUnreadCount(userId);
        req.setAttribute("recentNotifications", recentNotifications);
        req.setAttribute("unreadNotificationsCount", unreadNotifs);

        // Transfer flash messages if present in session
        String flashSuccess = (String) session.getAttribute("flashSuccess");
        if (flashSuccess != null) {
            req.setAttribute("flashSuccess", flashSuccess);
            session.removeAttribute("flashSuccess");
        }

        req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
    }
}

