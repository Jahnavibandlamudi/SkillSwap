package com.skillswap.controller;

import com.skillswap.model.ExchangeRequest;
import com.skillswap.model.User;
import com.skillswap.service.ExchangeService;
import com.skillswap.service.ExchangeService.RequestResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Skill Exchange Proposals (Module 5).
 */
@WebServlet(name = "ExchangeServlet", urlPatterns = {"/requests"})
public class ExchangeServlet extends HttpServlet {

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
        List<ExchangeRequest> received = exchangeService.getReceivedRequests(userId);
        List<ExchangeRequest> sent = exchangeService.getSentRequests(userId);
        List<ExchangeRequest> accepted = exchangeService.getAcceptedRequests(userId);

        req.setAttribute("receivedRequests", received);
        req.setAttribute("sentRequests", sent);
        req.setAttribute("acceptedRequests", accepted);

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

        req.getRequestDispatcher("/requests.jsp").forward(req, resp);
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

        if ("send".equals(action)) {
            try {
                int receiverId = Integer.parseInt(req.getParameter("receiverId"));
                int offeredSkillId = Integer.parseInt(req.getParameter("offeredSkillId"));
                int requestedSkillId = Integer.parseInt(req.getParameter("requestedSkillId"));
                String message = req.getParameter("message");

                RequestResult result = exchangeService.sendRequest(currentUser.getUserId(), receiverId,
                                                                   offeredSkillId, requestedSkillId, message);
                if (result.isSuccess()) {
                    session.setAttribute("flashSuccess", result.getMessage());
                } else {
                    session.setAttribute("errorMessage", result.getMessage());
                }
            } catch (Exception e) {
                session.setAttribute("errorMessage", "Invalid request parameters.");
            }
            resp.sendRedirect(req.getContextPath() + "/requests");

        } else if ("accept".equals(action)) {
            try {
                int requestId = Integer.parseInt(req.getParameter("requestId"));
                boolean ok = exchangeService.acceptRequest(requestId, currentUser.getUserId());
                if (ok) {
                    session.setAttribute("flashSuccess", "Exchange request accepted! You can now schedule learning sessions.");
                } else {
                    session.setAttribute("errorMessage", "Failed to accept request. Either unauthorized or not in pending state.");
                }
            } catch (Exception e) {
                session.setAttribute("errorMessage", "Invalid request.");
            }
            resp.sendRedirect(req.getContextPath() + "/requests");

        } else if ("reject".equals(action)) {
            try {
                int requestId = Integer.parseInt(req.getParameter("requestId"));
                boolean ok = exchangeService.rejectRequest(requestId, currentUser.getUserId());
                if (ok) {
                    session.setAttribute("flashSuccess", "Exchange request rejected.");
                } else {
                    session.setAttribute("errorMessage", "Failed to reject request.");
                }
            } catch (Exception e) {
                session.setAttribute("errorMessage", "Invalid request.");
            }
            resp.sendRedirect(req.getContextPath() + "/requests");

        } else if ("cancel".equals(action)) {
            try {
                int requestId = Integer.parseInt(req.getParameter("requestId"));
                boolean ok = exchangeService.cancelRequest(requestId, currentUser.getUserId());
                if (ok) {
                    session.setAttribute("flashSuccess", "Pending exchange request cancelled.");
                } else {
                    session.setAttribute("errorMessage", "Failed to cancel request.");
                }
            } catch (Exception e) {
                session.setAttribute("errorMessage", "Invalid request.");
            }
            resp.sendRedirect(req.getContextPath() + "/requests");
        }
    }
}

