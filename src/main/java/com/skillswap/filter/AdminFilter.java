package com.skillswap.filter;

import com.skillswap.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter ensuring only users with ADMIN role can access administrator endpoints.
 */
@WebFilter(filterName = "AdminFilter", urlPatterns = {"/admin", "/admin-dashboard"})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            req.getSession(true).setAttribute("errorMessage", "Admin login required.");
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (!user.isAdmin()) {
            req.getSession(true).setAttribute("errorMessage", "Access denied. Administrator privileges required.");
            res.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        chain.doFilter(request, response);
    }
}

