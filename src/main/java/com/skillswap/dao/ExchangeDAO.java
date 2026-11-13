package com.skillswap.dao;

import com.skillswap.model.ExchangeRequest;
import com.skillswap.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Skill Exchange Requests.
 */
public class ExchangeDAO {

    private static final Logger LOGGER = Logger.getLogger(ExchangeDAO.class.getName());

    private ExchangeRequest mapRequest(ResultSet rs) throws SQLException {
        ExchangeRequest req = new ExchangeRequest();
        req.setRequestId(rs.getInt("request_id"));
        req.setSenderId(rs.getInt("sender_id"));
        req.setReceiverId(rs.getInt("receiver_id"));
        req.setOfferedSkillId(rs.getInt("offered_skill_id"));
        req.setRequestedSkillId(rs.getInt("requested_skill_id"));
        req.setMessage(rs.getString("message"));
        req.setStatus(rs.getString("status"));
        req.setCreatedAt(rs.getTimestamp("created_at"));

        // Joined fields
        req.setSenderName(rs.getString("sender_name"));
        req.setSenderEmail(rs.getString("sender_email"));
        req.setSenderDepartment(rs.getString("sender_dept"));
        req.setSenderImage(rs.getString("sender_image"));

        req.setReceiverName(rs.getString("receiver_name"));
        req.setReceiverEmail(rs.getString("receiver_email"));
        req.setReceiverDepartment(rs.getString("receiver_dept"));
        req.setReceiverImage(rs.getString("receiver_image"));

        req.setOfferedSkillName(rs.getString("offered_skill_name"));
        req.setRequestedSkillName(rs.getString("requested_skill_name"));
        return req;
    }

    private static final String BASE_SELECT =
        "SELECT er.*, " +
        "u1.full_name AS sender_name, u1.email AS sender_email, u1.department AS sender_dept, u1.profile_image AS sender_image, " +
        "u2.full_name AS receiver_name, u2.email AS receiver_email, u2.department AS receiver_dept, u2.profile_image AS receiver_image, " +
        "s1.skill_name AS offered_skill_name, s2.skill_name AS requested_skill_name " +
        "FROM exchange_requests er " +
        "JOIN users u1 ON er.sender_id = u1.user_id " +
        "JOIN users u2 ON er.receiver_id = u2.user_id " +
        "JOIN skills s1 ON er.offered_skill_id = s1.skill_id " +
        "JOIN skills s2 ON er.requested_skill_id = s2.skill_id ";

    public int createRequest(ExchangeRequest request) {
        String sql = "INSERT INTO exchange_requests (sender_id, receiver_id, offered_skill_id, requested_skill_id, message, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, request.getSenderId());
            ps.setInt(2, request.getReceiverId());
            ps.setInt(3, request.getOfferedSkillId());
            ps.setInt(4, request.getRequestedSkillId());
            ps.setString(5, request.getMessage());
            ps.setString(6, request.getStatus() != null ? request.getStatus() : "PENDING");
            int rows = ps.executeUpdate();
            if (rows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting exchange request", e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return -1;
    }

    public ExchangeRequest findById(int requestId) {
        String sql = BASE_SELECT + "WHERE er.request_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, requestId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRequest(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding request by ID: " + requestId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return null;
    }

    public List<ExchangeRequest> findByReceiver(int receiverId) {
        List<ExchangeRequest> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE er.receiver_id = ? ORDER BY er.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, receiverId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRequest(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding requests received by: " + receiverId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    public List<ExchangeRequest> findBySender(int senderId) {
        List<ExchangeRequest> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE er.sender_id = ? ORDER BY er.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, senderId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRequest(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding requests sent by: " + senderId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    public boolean hasPendingRequest(int senderId, int receiverId, int offeredSkillId, int requestedSkillId) {
        String sql = "SELECT COUNT(*) FROM exchange_requests " +
                     "WHERE sender_id = ? AND receiver_id = ? AND offered_skill_id = ? AND requested_skill_id = ? " +
                     "AND status = 'PENDING'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, senderId);
            ps.setInt(2, receiverId);
            ps.setInt(3, offeredSkillId);
            ps.setInt(4, requestedSkillId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking pending request", e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return false;
    }

    public boolean updateStatus(int requestId, String status) {
        String sql = "UPDATE exchange_requests SET status = ? WHERE request_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, requestId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating request status: " + requestId, e);
        } finally {
            DBConnection.closeQuietly(null, ps, conn);
        }
        return false;
    }

    public List<ExchangeRequest> findAcceptedRequestsForUser(int userId) {
        List<ExchangeRequest> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE (er.sender_id = ? OR er.receiver_id = ?) AND er.status = 'ACCEPTED' ORDER BY er.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRequest(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding accepted requests for user: " + userId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    public List<ExchangeRequest> findAllRequests() {
        List<ExchangeRequest> list = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY er.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRequest(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all exchange requests", e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    public int getTotalRequestCount() {
        String sql = "SELECT COUNT(*) FROM exchange_requests";
        return executeCount(sql);
    }

    public int getAcceptedRequestCount() {
        String sql = "SELECT COUNT(*) FROM exchange_requests WHERE status = 'ACCEPTED'";
        return executeCount(sql);
    }

    private int executeCount(String sql) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting requests: " + sql, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return 0;
    }
}

