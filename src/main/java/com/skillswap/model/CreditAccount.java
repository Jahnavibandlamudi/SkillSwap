package com.skillswap.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing student's virtual credit wallet.
 */
public class CreditAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    private int accountId;
    private int userId;
    private int balance;
    private Timestamp updatedAt;

    public CreditAccount() {
        this.balance = 5; // 5 welcome credits default
    }

    public CreditAccount(int accountId, int userId, int balance, Timestamp updatedAt) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
        this.updatedAt = updatedAt;
    }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getBalance() { return balance; }
    public void setBalance(int balance) { this.balance = balance; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}

