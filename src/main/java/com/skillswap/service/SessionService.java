package com.skillswap.service;

import com.skillswap.dao.CreditDAO;
import com.skillswap.dao.ExchangeDAO;
import com.skillswap.dao.NotificationDAO;
import com.skillswap.dao.ReviewDAO;
import com.skillswap.dao.SessionDAO;
import com.skillswap.dao.SkillDAO;
import com.skillswap.dao.UserDAO;
import com.skillswap.model.ExchangeRequest;
import com.skillswap.model.LearningSession;
import com.skillswap.model.Notification;
import com.skillswap.model.Review;
import com.skillswap.model.Skill;
import com.skillswap.model.User;
import com.skillswap.util.ValidationUtil;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * Service for Learning Session Management (Module 6 & Module 7).
 * Implements two-way completion confirmation and triggers atomic credit transfer.
 */
public class SessionService {

    private final SessionDAO sessionDAO = new SessionDAO();
    private final ExchangeDAO exchangeDAO = new ExchangeDAO();
    private final CreditDAO creditDAO = new CreditDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final UserDAO userDAO = new UserDAO();
    private final SkillDAO skillDAO = new SkillDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();

    public static class SessionResult {
        private final boolean success;
        private final String message;
        private final LearningSession session;

        public SessionResult(boolean success, String message, LearningSession session) {
            this.success = success;
            this.message = message;
            this.session = session;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public LearningSession getSession() { return session; }
    }

    /**
     * Schedules a learning session linked to an accepted exchange request.
     */
    public SessionResult scheduleSession(int requestId, int currentUserId, int teacherId, int learnerId,
                                         int skillId, Date sessionDate, Time startTime, Time endTime,
                                         String meetingMode, String meetingDetails) {

        // 1. Validate accepted exchange request
        ExchangeRequest req = exchangeDAO.findById(requestId);
        if (req == null || !req.isAccepted()) {
            return new SessionResult(false, "Sessions can only be scheduled for accepted exchange requests.", null);
        }

        // Verify current user is part of the exchange
        if (req.getSenderId() != currentUserId && req.getReceiverId() != currentUserId) {
            return new SessionResult(false, "You are not authorized to schedule a session for this request.", null);
        }

        // Verify teacher and learner belong to the exchange
        if ((teacherId != req.getSenderId() && teacherId != req.getReceiverId()) ||
            (learnerId != req.getSenderId() && learnerId != req.getReceiverId()) ||
            teacherId == learnerId) {
            return new SessionResult(false, "Invalid teacher or learner assigned to this session.", null);
        }

        // 2. Validate timing
        if (sessionDate == null || startTime == null || endTime == null) {
            return new SessionResult(false, "Please specify valid date, start time, and end time.", null);
        }

        if (endTime.before(startTime) || endTime.equals(startTime)) {
            return new SessionResult(false, "End time must be strictly after start time.", null);
        }

        // 3. Validate meeting details
        if (!ValidationUtil.isNotEmpty(meetingDetails)) {
            return new SessionResult(false, "Please provide meeting details (Google Meet link or SRM AP Campus Venue).", null);
        }

        // 4. Validate learner credit balance (must have at least 2 credits)
        int learnerBalance = creditDAO.getBalance(learnerId);
        if (learnerBalance < 2) {
            User learner = userDAO.findById(learnerId);
            String name = learner != null ? learner.getFullName() : "Learner";
            return new SessionResult(false, name + " does not have sufficient credits (2 credits required to attend).", null);
        }

        // 5. Overlapping bookings prevention
        if (sessionDAO.hasConflictingSession(teacherId, sessionDate, startTime, endTime, 0)) {
            User teacher = userDAO.findById(teacherId);
            return new SessionResult(false, (teacher != null ? teacher.getFullName() : "Teacher") + " has a scheduling conflict during this time slot.", null);
        }

        if (sessionDAO.hasConflictingSession(learnerId, sessionDate, startTime, endTime, 0)) {
            User learner = userDAO.findById(learnerId);
            return new SessionResult(false, (learner != null ? learner.getFullName() : "Learner") + " has a scheduling conflict during this time slot.", null);
        }

        // 6. Create session
        LearningSession session = new LearningSession();
        session.setRequestId(requestId);
        session.setTeacherId(teacherId);
        session.setLearnerId(learnerId);
        session.setSkillId(skillId);
        session.setSessionDate(sessionDate);
        session.setStartTime(startTime);
        session.setEndTime(endTime);
        session.setMeetingMode(ValidationUtil.isNotEmpty(meetingMode) ? meetingMode : "ONLINE");
        session.setMeetingDetails(ValidationUtil.sanitize(meetingDetails));
        session.setStatus("SCHEDULED");

        int sessionId = sessionDAO.createSession(session);
        if (sessionId <= 0) {
            return new SessionResult(false, "Failed to schedule session in database.", null);
        }
        session.setSessionId(sessionId);

        // 7. Notify other participant
        int otherId = (currentUserId == teacherId) ? learnerId : teacherId;
        User me = userDAO.findById(currentUserId);
        Skill sk = skillDAO.findById(skillId);

        Notification notif = new Notification();
        notif.setUserId(otherId);
        notif.setMessage(me.getFullName() + " scheduled a learning session for " +
                         (sk != null ? sk.getSkillName() : "Skill") + " on " + sessionDate + " at " + startTime + ".");
        notif.setType("SESSION");
        notif.setLink("sessions?action=view&id=" + sessionId);
        notificationDAO.createNotification(notif);

        return new SessionResult(true, "Session scheduled successfully!", session);
    }

    /**
     * Confirms session completion.
     * Requires confirmations from both teacher and learner to transition status to COMPLETED
     * and trigger atomic credit transfer!
     */
    public SessionResult confirmCompletion(int sessionId, int currentUserId) {
        LearningSession session = sessionDAO.findById(sessionId);
        if (session == null) {
            return new SessionResult(false, "Session not found.", null);
        }

        if (!session.isScheduled()) {
            return new SessionResult(false, "This session is not in scheduled state.", null);
        }

        boolean isTeacher = (session.getTeacherId() == currentUserId);
        boolean isLearner = (session.getLearnerId() == currentUserId);

        if (!isTeacher && !isLearner) {
            return new SessionResult(false, "You are not a participant in this session.", null);
        }

        // Update confirmation
        boolean updated = sessionDAO.setConfirmation(sessionId, isTeacher, true);
        if (!updated) {
            return new SessionResult(false, "Failed to update session confirmation.", null);
        }

        // Reload fresh state
        session = sessionDAO.findById(sessionId);

        // Check if both have confirmed
        if (session.isTeacherConfirmed() && session.isLearnerConfirmed()) {
            // Mark session COMPLETED
            sessionDAO.updateStatus(sessionId, "COMPLETED");

            // Execute atomic credit transfer (2 credits from learner to teacher)
            boolean creditOk = creditDAO.executeSessionCreditTransfer(
                session.getTeacherId(),
                session.getLearnerId(),
                sessionId,
                2,
                session.getSkillName(),
                session.getTeacherName(),
                session.getLearnerName()
            );

            if (!creditOk) {
                return new SessionResult(true, "Session confirmed by both, but credit transfer encountered an issue (e.g. Insufficient balance or already processed).", session);
            }

            return new SessionResult(true, "Both participants confirmed! Session completed and 2 credits transferred.", session);
        } else {
            // Notify the other participant to confirm
            int otherId = isTeacher ? session.getLearnerId() : session.getTeacherId();
            String roleStr = isTeacher ? "Teacher" : "Learner";
            Notification notif = new Notification();
            notif.setUserId(otherId);
            notif.setMessage("Your session partner confirmed completion of the session on " + session.getSkillName() +
                             ". Please confirm your completion to finalize credits and leave feedback.");
            notif.setType("SESSION");
            notif.setLink("sessions?action=view&id=" + sessionId);
            notificationDAO.createNotification(notif);

            return new SessionResult(true, "Your confirmation recorded! Awaiting confirmation from your session partner.", session);
        }
    }

    public boolean cancelSession(int sessionId, int currentUserId) {
        LearningSession session = sessionDAO.findById(sessionId);
        if (session == null || !session.isScheduled()) {
            return false;
        }
        if (session.getTeacherId() != currentUserId && session.getLearnerId() != currentUserId) {
            return false;
        }
        boolean ok = sessionDAO.updateStatus(sessionId, "CANCELLED");
        if (ok) {
            int otherId = (session.getTeacherId() == currentUserId) ? session.getLearnerId() : session.getTeacherId();
            User me = userDAO.findById(currentUserId);
            Notification notif = new Notification();
            notif.setUserId(otherId);
            notif.setMessage((me != null ? me.getFullName() : "Partner") + " cancelled the scheduled session on " + session.getSkillName() + ".");
            notif.setType("SESSION");
            notif.setLink("sessions");
            notificationDAO.createNotification(notif);
        }
        return ok;
    }

    public LearningSession getSessionById(int sessionId, int currentUserId) {
        LearningSession session = sessionDAO.findById(sessionId);
        if (session != null && currentUserId > 0) {
            Review rev = reviewDAO.findBySessionAndReviewer(sessionId, currentUserId);
            session.setUserHasReviewed(rev != null);
        }
        return session;
    }

    public List<LearningSession> getUserSessions(int userId) {
        List<LearningSession> list = sessionDAO.findByUserId(userId);
        for (LearningSession s : list) {
            Review rev = reviewDAO.findBySessionAndReviewer(s.getSessionId(), userId);
            s.setUserHasReviewed(rev != null);
        }
        return list;
    }

    public List<LearningSession> getUpcomingSessions(int userId) {
        return sessionDAO.findUpcomingByUserId(userId);
    }

    public List<LearningSession> getCompletedSessions(int userId) {
        List<LearningSession> list = sessionDAO.findCompletedByUserId(userId);
        for (LearningSession s : list) {
            Review rev = reviewDAO.findBySessionAndReviewer(s.getSessionId(), userId);
            s.setUserHasReviewed(rev != null);
        }
        return list;
    }
}

