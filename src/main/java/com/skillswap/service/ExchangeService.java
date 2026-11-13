package com.skillswap.service;

import com.skillswap.dao.ExchangeDAO;
import com.skillswap.dao.NotificationDAO;
import com.skillswap.dao.SkillDAO;
import com.skillswap.dao.UserDAO;
import com.skillswap.model.ExchangeRequest;
import com.skillswap.model.Notification;
import com.skillswap.model.Skill;
import com.skillswap.model.User;
import com.skillswap.util.ValidationUtil;

import java.util.List;

/**
 * Service for Skill Exchange Requests (Module 5).
 */
public class ExchangeService {

    private final ExchangeDAO exchangeDAO = new ExchangeDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final UserDAO userDAO = new UserDAO();
    private final SkillDAO skillDAO = new SkillDAO();

    public static class RequestResult {
        private final boolean success;
        private final String message;
        private final ExchangeRequest request;

        public RequestResult(boolean success, String message, ExchangeRequest request) {
            this.success = success;
            this.message = message;
            this.request = request;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public ExchangeRequest getRequest() { return request; }
    }

    public RequestResult sendRequest(int senderId, int receiverId, int offeredSkillId,
                                    int requestedSkillId, String message) {
        // 1. Validation: cannot send to self
        if (senderId == receiverId) {
            return new RequestResult(false, "You cannot send an exchange request to yourself.", null);
        }

        if (senderId <= 0 || receiverId <= 0 || offeredSkillId <= 0 || requestedSkillId <= 0) {
            return new RequestResult(false, "Invalid exchange request parameters.", null);
        }

        User receiver = userDAO.findById(receiverId);
        if (receiver == null || !receiver.isActive()) {
            return new RequestResult(false, "The recipient student is inactive or does not exist.", null);
        }

        User sender = userDAO.findById(senderId);

        // 2. Prevent duplicate pending requests
        if (exchangeDAO.hasPendingRequest(senderId, receiverId, offeredSkillId, requestedSkillId)) {
            return new RequestResult(false, "A pending exchange request with these exact skills already exists between you two.", null);
        }

        Skill offeredSkill = skillDAO.findById(offeredSkillId);
        Skill requestedSkill = skillDAO.findById(requestedSkillId);

        ExchangeRequest req = new ExchangeRequest();
        req.setSenderId(senderId);
        req.setReceiverId(receiverId);
        req.setOfferedSkillId(offeredSkillId);
        req.setRequestedSkillId(requestedSkillId);
        req.setMessage(ValidationUtil.sanitize(message));
        req.setStatus("PENDING");

        int reqId = exchangeDAO.createRequest(req);
        if (reqId <= 0) {
            return new RequestResult(false, "Failed to submit exchange request. Please try again.", null);
        }
        req.setRequestId(reqId);

        // 3. Notify receiver
        Notification notif = new Notification();
        notif.setUserId(receiverId);
        notif.setMessage(sender.getFullName() + " sent you a skill exchange request: Teaching " +
                         (offeredSkill != null ? offeredSkill.getSkillName() : "Skill") + " for " +
                         (requestedSkill != null ? requestedSkill.getSkillName() : "Skill") + ".");
        notif.setType("REQUEST");
        notif.setLink("requests");
        notificationDAO.createNotification(notif);

        return new RequestResult(true, "Skill exchange request sent successfully!", req);
    }

    public boolean acceptRequest(int requestId, int currentUserId) {
        ExchangeRequest req = exchangeDAO.findById(requestId);
        if (req == null) return false;

        // Security check: Only the intended receiver can accept
        if (req.getReceiverId() != currentUserId) {
            return false;
        }

        if (!req.isPending()) {
            return false;
        }

        boolean ok = exchangeDAO.updateStatus(requestId, "ACCEPTED");
        if (ok) {
            Notification notif = new Notification();
            notif.setUserId(req.getSenderId());
            notif.setMessage(req.getReceiverName() + " accepted your skill exchange request! You can now schedule a learning session.");
            notif.setType("REQUEST");
            notif.setLink("sessions?action=new&requestId=" + requestId);
            notificationDAO.createNotification(notif);
        }
        return ok;
    }

    public boolean rejectRequest(int requestId, int currentUserId) {
        ExchangeRequest req = exchangeDAO.findById(requestId);
        if (req == null) return false;

        // Security check: Only the intended receiver can reject
        if (req.getReceiverId() != currentUserId) {
            return false;
        }

        if (!req.isPending()) {
            return false;
        }

        boolean ok = exchangeDAO.updateStatus(requestId, "REJECTED");
        if (ok) {
            Notification notif = new Notification();
            notif.setUserId(req.getSenderId());
            notif.setMessage(req.getReceiverName() + " declined your skill exchange request.");
            notif.setType("REQUEST");
            notif.setLink("requests");
            notificationDAO.createNotification(notif);
        }
        return ok;
    }

    public boolean cancelRequest(int requestId, int currentUserId) {
        ExchangeRequest req = exchangeDAO.findById(requestId);
        if (req == null) return false;

        // Security check: Only the sender can cancel their own pending request
        if (req.getSenderId() != currentUserId) {
            return false;
        }

        if (!req.isPending()) {
            return false;
        }

        return exchangeDAO.updateStatus(requestId, "CANCELLED");
    }

    public ExchangeRequest getRequestById(int requestId) {
        return exchangeDAO.findById(requestId);
    }

    public List<ExchangeRequest> getReceivedRequests(int userId) {
        return exchangeDAO.findByReceiver(userId);
    }

    public List<ExchangeRequest> getSentRequests(int userId) {
        return exchangeDAO.findBySender(userId);
    }

    public List<ExchangeRequest> getAcceptedRequests(int userId) {
        return exchangeDAO.findAcceptedRequestsForUser(userId);
    }
}

