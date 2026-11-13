package com.skillswap.controller;

import com.skillswap.model.CreditTransaction;
import com.skillswap.model.User;
import com.skillswap.service.CreditService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Virtual Credit Wallet and Transactions (Module 7).
 */
@WebServlet(name = "CreditServlet", urlPatterns = {"/credits"})
public class CreditServlet extends HttpServlet {

    private final CreditService creditService = new CreditService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int userId = currentUser.getUserId();
        int balance = creditService.getBalance(userId);
        List<CreditTransaction> transactions = creditService.getTransactions(userId);

        int totalEarned = transactions.stream()
            .filter(t -> t.getAmount() > 0)
            .mapToInt(CreditTransaction::getAmount)
            .sum();

        int totalSpent = transactions.stream()
            .filter(t -> t.getAmount() < 0)
            .mapToInt(t -> Math.abs(t.getAmount()))
            .sum();

        req.setAttribute("balance", balance);
        req.setAttribute("totalEarned", totalEarned);
        req.setAttribute("totalSpent", totalSpent);
        req.setAttribute("transactions", transactions);

        req.getRequestDispatcher("/credits.jsp").forward(req, resp);
    }
}

