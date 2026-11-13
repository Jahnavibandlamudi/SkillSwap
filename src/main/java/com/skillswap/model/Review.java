package com.skillswap.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing student feedback and ratings (1-5 stars).
 */
public class Review implements Serializable {
    private static final long serialVersionUID = 1L;

    private int reviewId;
    private int sessionId;
    private int reviewerId;
    private int reviewedUserId;
    private int rating; // 1 to 5
    private String comment;
    private Timestamp createdAt;

    // Joined fields for display
    private String reviewerName;
    private String reviewerImage;
    private String reviewedUserName;
    private String sessionSkillName;

    public Review() {}

    public Review(int reviewId, int sessionId, int reviewerId, int reviewedUserId,
                  int rating, String comment, Timestamp createdAt) {
        this.reviewId = reviewId;
        this.sessionId = sessionId;
        this.reviewerId = reviewerId;
        this.reviewedUserId = reviewedUserId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }

    public int getReviewerId() { return reviewerId; }
    public void setReviewerId(int reviewerId) { this.reviewerId = reviewerId; }

    public int getReviewedUserId() { return reviewedUserId; }
    public void setReviewedUserId(int reviewedUserId) { this.reviewedUserId = reviewedUserId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getReviewerName() { return reviewerName; }
    public void setReviewerName(String reviewerName) { this.reviewerName = reviewerName; }

    public String getReviewerImage() { return reviewerImage; }
    public void setReviewerImage(String reviewerImage) { this.reviewerImage = reviewerImage; }

    public String getReviewedUserName() { return reviewedUserName; }
    public void setReviewedUserName(String reviewedUserName) { this.reviewedUserName = reviewedUserName; }

    public String getSessionSkillName() { return sessionSkillName; }
    public void setSessionSkillName(String sessionSkillName) { this.sessionSkillName = sessionSkillName; }
}

