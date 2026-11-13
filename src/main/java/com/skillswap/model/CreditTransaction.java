package com.skillswap.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing an immutable ledger transaction of virtual platform credits.
 */
public class CreditTransaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private int transactionId;
    private int userId;
    private Integer sessionId; // nullable for welcome bonus
    private int amount;        // positive for earn/welcome, negative for spend
    private String transactionType; // 'WELCOME', 'EARN', 'SPEND'
    private String description;
    private Timestamp createdAt;

    public CreditTransaction() {}

    public CreditTransaction(int transactionId, int userId, Integer sessionId, int amount,
                             String transactionType, String description, Timestamp createdAt) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.sessionId = sessionId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.description = description;
        this.createdAt = createdAt;
    }

    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Integer getSessionId() { return sessionId; }
    public void setSessionId(Integer sessionId) { this.sessionId = sessionId; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public boolean isCreditEarned() {
        return "EARN".equalsIgnoreCase(transactionType) || "WELCOME".equalsIgnoreCase(transactionType);
    }
}

