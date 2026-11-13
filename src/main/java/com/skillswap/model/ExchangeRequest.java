package com.skillswap.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing a peer skill exchange proposal between two students.
 */
public class ExchangeRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private int requestId;
    private int senderId;
    private int receiverId;
    private int offeredSkillId;
    private int requestedSkillId;
    private String message;
    private String status;  // 'PENDING', 'ACCEPTED', 'REJECTED', 'CANCELLED'
    private Timestamp createdAt;

    // Joined fields for display
    private String senderName;
    private String senderEmail;
    private String senderDepartment;
    private String senderImage;
    private String receiverName;
    private String receiverEmail;
    private String receiverDepartment;
    private String receiverImage;
    private String offeredSkillName;
    private String requestedSkillName;

    public ExchangeRequest() {
        this.status = "PENDING";
    }

    public ExchangeRequest(int requestId, int senderId, int receiverId, int offeredSkillId,
                           int requestedSkillId, String message, String status, Timestamp createdAt) {
        this.requestId = requestId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.offeredSkillId = offeredSkillId;
        this.requestedSkillId = requestedSkillId;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }

    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }

    public int getOfferedSkillId() { return offeredSkillId; }
    public void setOfferedSkillId(int offeredSkillId) { this.offeredSkillId = offeredSkillId; }

    public int getRequestedSkillId() { return requestedSkillId; }
    public void setRequestedSkillId(int requestedSkillId) { this.requestedSkillId = requestedSkillId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }

    public String getSenderDepartment() { return senderDepartment; }
    public void setSenderDepartment(String senderDepartment) { this.senderDepartment = senderDepartment; }

    public String getSenderImage() { return senderImage; }
    public void setSenderImage(String senderImage) { this.senderImage = senderImage; }

    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }

    public String getReceiverEmail() { return receiverEmail; }
    public void setReceiverEmail(String receiverEmail) { this.receiverEmail = receiverEmail; }

    public String getReceiverDepartment() { return receiverDepartment; }
    public void setReceiverDepartment(String receiverDepartment) { this.receiverDepartment = receiverDepartment; }

    public String getReceiverImage() { return receiverImage; }
    public void setReceiverImage(String receiverImage) { this.receiverImage = receiverImage; }

    public String getOfferedSkillName() { return offeredSkillName; }
    public void setOfferedSkillName(String offeredSkillName) { this.offeredSkillName = offeredSkillName; }

    public String getRequestedSkillName() { return requestedSkillName; }
    public void setRequestedSkillName(String requestedSkillName) { this.requestedSkillName = requestedSkillName; }

    public boolean isPending() { return "PENDING".equalsIgnoreCase(status); }
    public boolean isAccepted() { return "ACCEPTED".equalsIgnoreCase(status); }
    public boolean isRejected() { return "REJECTED".equalsIgnoreCase(status); }
    public boolean isCancelled() { return "CANCELLED".equalsIgnoreCase(status); }
}

