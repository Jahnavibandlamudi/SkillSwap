package com.skillswap.dao;

import com.skillswap.model.Review;
import com.skillswap.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Reviews and Ratings.
 */
public class ReviewDAO {

    private static final Logger LOGGER = Logger.getLogger(ReviewDAO.class.getName());

    public int createReview(Review review) {
        String sql = "INSERT INTO reviews (session_id, reviewer_id, reviewed_user_id, rating, comment) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, review.getSessionId());
            ps.setInt(2, review.getReviewerId());
            ps.setInt(3, review.getReviewedUserId());
            ps.setInt(4, review.getRating());
            ps.setString(5, review.getComment());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting review", e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return -1;
    }

    public Review findBySessionAndReviewer(int sessionId, int reviewerId) {
        String sql = "SELECT * FROM reviews WHERE session_id = ? AND reviewer_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sessionId);
            ps.setInt(2, reviewerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Review r = new Review();
                r.setReviewId(rs.getInt("review_id"));
                r.setSessionId(rs.getInt("session_id"));
                r.setReviewerId(rs.getInt("reviewer_id"));
                r.setReviewedUserId(rs.getInt("reviewed_user_id"));
                r.setRating(rs.getInt("rating"));
                r.setComment(rs.getString("comment"));
                r.setCreatedAt(rs.getTimestamp("created_at"));
                return r;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking review existence", e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return null;
    }

    public List<Review> findByReviewedUser(int userId) {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name AS reviewer_name, u.profile_image AS reviewer_image, " +
                     "s.skill_name AS session_skill_name " +
                     "FROM reviews r " +
                     "JOIN users u ON r.reviewer_id = u.user_id " +
                     "JOIN learning_sessions ls ON r.session_id = ls.session_id " +
                     "JOIN skills s ON ls.skill_id = s.skill_id " +
                     "WHERE r.reviewed_user_id = ? ORDER BY r.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Review r = new Review();
                r.setReviewId(rs.getInt("review_id"));
                r.setSessionId(rs.getInt("session_id"));
                r.setReviewerId(rs.getInt("reviewer_id"));
                r.setReviewedUserId(rs.getInt("reviewed_user_id"));
                r.setRating(rs.getInt("rating"));
                r.setComment(rs.getString("comment"));
                r.setCreatedAt(rs.getTimestamp("created_at"));
                r.setReviewerName(rs.getString("reviewer_name"));
                r.setReviewerImage(rs.getString("reviewer_image"));
                r.setSessionSkillName(rs.getString("session_skill_name"));
                list.add(r);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding reviews for user: " + userId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    public double getAverageRating(int userId) {
        String sql = "SELECT AVG(rating) FROM reviews WHERE reviewed_user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                double avg = rs.getDouble(1);
                return Math.round(avg * 10.0) / 10.0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error calculating average rating for user: " + userId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return 0.0;
    }

    public int getTotalReviewCount() {
        String sql = "SELECT COUNT(*) FROM reviews";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting reviews", e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return 0;
    }
}

