package com.skillswap.controller;

import com.skillswap.model.User;
import com.skillswap.service.ReviewService;
import com.skillswap.service.ReviewService.ReviewResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller for Peer Ratings and Reviews (Module 8).
 */
@WebServlet(name = "ReviewServlet", urlPatterns = {"/reviews"})
public class ReviewServlet extends HttpServlet {

    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            int sessionId = Integer.parseInt(req.getParameter("sessionId"));
            int rating = Integer.parseInt(req.getParameter("rating"));
            String comment = req.getParameter("comment");

            ReviewResult result = reviewService.submitReview(sessionId, currentUser.getUserId(), rating, comment);
            if (result.isSuccess()) {
                session.setAttribute("flashSuccess", result.getMessage());
            } else {
                session.setAttribute("errorMessage", result.getMessage());
            }
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error submitting review: Please provide a valid rating (1 to 5 stars).");
        }

        resp.sendRedirect(req.getContextPath() + "/sessions");
    }
}

