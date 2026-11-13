package com.skillswap.controller;

import com.skillswap.model.User;
import com.skillswap.service.AuthService;
import com.skillswap.service.AuthService.AuthResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller handling Student and Admin Authentication (Login, Register, Logout).
 */
@WebServlet(name = "AuthServlet", urlPatterns = {"/login", "/register", "/logout"})
public class AuthServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession(false);

        if ("/logout".equals(path)) {
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/login?message=logged_out");
            return;
        }

        // If user is already logged in, redirect to dashboard
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if (user.isAdmin()) {
                resp.sendRedirect(req.getContextPath() + "/admin");
            } else {
                resp.sendRedirect(req.getContextPath() + "/dashboard");
            }
            return;
        }

        if ("/register".equals(path)) {
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/login".equals(path)) {
            handleLogin(req, resp);
        } else if ("/register".equals(path)) {
            handleRegister(req, resp);
        } else if ("/logout".equals(path)) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/login?message=logged_out");
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String identifier = req.getParameter("identifier");
        String password = req.getParameter("password");

        AuthResult result = authService.login(identifier, password);

        if (result.isSuccess()) {
            HttpSession session = req.getSession(true);
            session.setAttribute("user", result.getUser());
            session.setMaxInactiveInterval(30 * 60); // 30 minutes

            if (result.getUser().isAdmin()) {
                resp.sendRedirect(req.getContextPath() + "/admin");
            } else {
                resp.sendRedirect(req.getContextPath() + "/dashboard");
            }
        } else {
            req.setAttribute("errorMessage", result.getMessage());
            req.setAttribute("identifier", identifier);
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentId = req.getParameter("studentId");
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String department = req.getParameter("department");
        String yearStr = req.getParameter("yearOfStudy");
        String bio = req.getParameter("bio");

        int yearOfStudy = 1;
        try {
            if (yearStr != null) yearOfStudy = Integer.parseInt(yearStr.trim());
        } catch (NumberFormatException ignored) {}

        AuthResult result = authService.register(studentId, fullName, email, password,
                                                 confirmPassword, department, yearOfStudy, bio);

        if (result.isSuccess()) {
            HttpSession session = req.getSession(true);
            session.setAttribute("user", result.getUser());
            session.setAttribute("flashSuccess", "Registration successful! 5 Welcome Credits have been added to your wallet.");
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } else {
            req.setAttribute("errorMessage", result.getMessage());
            req.setAttribute("studentId", studentId);
            req.setAttribute("fullName", fullName);
            req.setAttribute("email", email);
            req.setAttribute("department", department);
            req.setAttribute("yearOfStudy", yearOfStudy);
            req.setAttribute("bio", bio);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }
}

