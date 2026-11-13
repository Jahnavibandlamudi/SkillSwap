package com.skillswap.service;

import com.skillswap.dao.CreditDAO;
import com.skillswap.model.CreditAccount;
import com.skillswap.model.CreditTransaction;

import java.util.List;

/**
 * Service for managing virtual learning credits and transaction ledger.
 */
public class CreditService {

    private final CreditDAO creditDAO = new CreditDAO();

    public int getBalance(int userId) {
        return creditDAO.getBalance(userId);
    }

    public CreditAccount getAccount(int userId) {
        return creditDAO.getAccount(userId);
    }

    public List<CreditTransaction> getTransactions(int userId) {
        return creditDAO.getTransactions(userId);
    }
}

