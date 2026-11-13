package com.skillswap.dao;

import com.skillswap.model.User;
import com.skillswap.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for User entities.
 */
public class UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setStudentId(rs.getString("student_id"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setCollege(rs.getString("college"));
        u.setDepartment(rs.getString("department"));
        u.setYearOfStudy(rs.getInt("year_of_study"));
        u.setBio(rs.getString("bio"));
        u.setProfileImage(rs.getString("profile_image"));
        u.setRole(rs.getString("role"));
        u.setStatus(rs.getString("status"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        return u;
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE LOWER(email) = LOWER(?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email != null ? email.trim() : "");
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding user by email: " + email, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return null;
    }

    public User findByStudentId(String studentId) {
        String sql = "SELECT * FROM users WHERE UPPER(student_id) = UPPER(?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, studentId != null ? studentId.trim() : "");
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding user by studentId: " + studentId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return null;
    }

    public User findById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding user by ID: " + userId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return null;
    }

    public int createUser(User user) {
        String sql = "INSERT INTO users (student_id, full_name, email, password_hash, college, department, year_of_study, bio, profile_image, role, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getStudentId());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPasswordHash());
            ps.setString(5, user.getCollege() != null ? user.getCollege() : "SRM UNIVERSITY AP");
            ps.setString(6, user.getDepartment());
            ps.setInt(7, user.getYearOfStudy());
            ps.setString(8, user.getBio());
            ps.setString(9, user.getProfileImage() != null ? user.getProfileImage() : "default-avatar.png");
            ps.setString(10, user.getRole() != null ? user.getRole() : "STUDENT");
            ps.setString(11, user.getStatus() != null ? user.getStatus() : "ACTIVE");

            int rows = ps.executeUpdate();
            if (rows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    user.setUserId(generatedId);
                    return generatedId;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating user: " + user.getEmail(), e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return -1;
    }

    public boolean updateProfile(User user) {
        String sql = "UPDATE users SET full_name = ?, department = ?, year_of_study = ?, bio = ?, profile_image = ? WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getDepartment());
            ps.setInt(3, user.getYearOfStudy());
            ps.setString(4, user.getBio());
            ps.setString(5, user.getProfileImage());
            ps.setInt(6, user.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating profile for user: " + user.getUserId(), e);
        } finally {
            DBConnection.closeQuietly(null, ps, conn);
        }
        return false;
    }

    public boolean updatePassword(int userId, String newHash) {
        String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, newHash);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating password for user: " + userId, e);
        } finally {
            DBConnection.closeQuietly(null, ps, conn);
        }
        return false;
    }

    public boolean updateStatus(int userId, String status) {
        String sql = "UPDATE users SET status = ? WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating status for user: " + userId, e);
        } finally {
            DBConnection.closeQuietly(null, ps, conn);
        }
        return false;
    }

    public List<User> findAllStudents() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = 'STUDENT' ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapUser(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching students", e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    public List<User> searchStudents(String query) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT DISTINCT u.* FROM users u " +
                     "LEFT JOIN user_skills us ON u.user_id = us.user_id " +
                     "LEFT JOIN skills s ON us.skill_id = s.skill_id " +
                     "WHERE u.role = 'STUDENT' AND (" +
                     "LOWER(u.full_name) LIKE LOWER(?) OR " +
                     "LOWER(u.student_id) LIKE LOWER(?) OR " +
                     "LOWER(u.department) LIKE LOWER(?) OR " +
                     "LOWER(s.skill_name) LIKE LOWER(?))";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            String term = "%" + query.trim() + "%";
            ps.setString(1, term);
            ps.setString(2, term);
            ps.setString(3, term);
            ps.setString(4, term);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapUser(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching students: " + query, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    public int getStudentCount() {
        String sql = "SELECT COUNT(*) FROM users WHERE role = 'STUDENT'";
        return executeCount(sql);
    }

    public int getActiveStudentCount() {
        String sql = "SELECT COUNT(*) FROM users WHERE role = 'STUDENT' AND status = 'ACTIVE'";
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
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error executing count query: " + sql, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return 0;
    }
}

