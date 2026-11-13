package com.skillswap.filter;

import com.skillswap.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter ensuring authenticated session for student/admin protected resources.
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {
    "/dashboard", "/profile", "/skills", "/matches", "/requests",
    "/sessions", "/credits", "/notifications", "/reviews"
})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            req.getSession(true).setAttribute("errorMessage", "Please log in to access this page.");
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}

