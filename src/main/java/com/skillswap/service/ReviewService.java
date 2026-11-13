package com.skillswap.service;

import com.skillswap.dao.NotificationDAO;
import com.skillswap.dao.ReviewDAO;
import com.skillswap.dao.SessionDAO;
import com.skillswap.dao.UserDAO;
import com.skillswap.model.LearningSession;
import com.skillswap.model.Notification;
import com.skillswap.model.Review;
import com.skillswap.model.User;
import com.skillswap.util.ValidationUtil;

import java.util.List;

/**
 * Service for Reviews and Ratings (Module 8).
 */
public class ReviewService {

    private final ReviewDAO reviewDAO = new ReviewDAO();
    private final SessionDAO sessionDAO = new SessionDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final UserDAO userDAO = new UserDAO();

    public static class ReviewResult {
        private final boolean success;
        private final String message;
        private final Review review;

        public ReviewResult(boolean success, String message, Review review) {
            this.success = success;
            this.message = message;
            this.review = review;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Review getReview() { return review; }
    }

    public ReviewResult submitReview(int sessionId, int reviewerId, int rating, String comment) {
        // 1. Validate rating bounds
        if (rating < 1 || rating > 5) {
            return new ReviewResult(false, "Rating must be between 1 and 5 stars.", null);
        }

        // 2. Validate session exists and is completed
        LearningSession session = sessionDAO.findById(sessionId);
        if (session == null) {
            return new ReviewResult(false, "Session not found.", null);
        }

        if (!session.isCompleted()) {
            return new ReviewResult(false, "Feedback can only be submitted for completed learning sessions.", null);
        }

        // 3. Verify reviewer is a participant
        if (session.getTeacherId() != reviewerId && session.getLearnerId() != reviewerId) {
            return new ReviewResult(false, "You are not a participant in this learning session.", null);
        }

        // 4. Identify recipient (the other participant)
        int reviewedUserId = (session.getTeacherId() == reviewerId) ? session.getLearnerId() : session.getTeacherId();

        // Prevent self review
        if (reviewerId == reviewedUserId) {
            return new ReviewResult(false, "You cannot review yourself.", null);
        }

        // 5. Prevent duplicate review for the same session and reviewer
        Review existing = reviewDAO.findBySessionAndReviewer(sessionId, reviewerId);
        if (existing != null) {
            return new ReviewResult(false, "You have already submitted a review for this session.", null);
        }

        Review review = new Review();
        review.setSessionId(sessionId);
        review.setReviewerId(reviewerId);
        review.setReviewedUserId(reviewedUserId);
        review.setRating(rating);
        review.setComment(ValidationUtil.sanitize(comment));

        int reviewId = reviewDAO.createReview(review);
        if (reviewId <= 0) {
            return new ReviewResult(false, "Failed to submit review. Please try again.", null);
        }
        review.setReviewId(reviewId);

        // 6. Notify reviewed user
        User reviewer = userDAO.findById(reviewerId);
        Notification notif = new Notification();
        notif.setUserId(reviewedUserId);
        notif.setMessage((reviewer != null ? reviewer.getFullName() : "A peer") + " left you a " + rating +
                         "-star review for your session on " + session.getSkillName() + "!");
        notif.setType("REVIEW");
        notif.setLink("profile?id=" + reviewedUserId);
        notificationDAO.createNotification(notif);

        return new ReviewResult(true, "Thank you! Your feedback has been recorded.", review);
    }

    public List<Review> getUserReviews(int userId) {
        return reviewDAO.findByReviewedUser(userId);
    }

    public double getAverageRating(int userId) {
        return reviewDAO.getAverageRating(userId);
    }
}

