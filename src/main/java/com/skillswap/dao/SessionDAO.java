package com.skillswap.dao;

import com.skillswap.model.LearningSession;
import com.skillswap.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Learning Sessions.
 */
public class SessionDAO {

    private static final Logger LOGGER = Logger.getLogger(SessionDAO.class.getName());

    private LearningSession mapSession(ResultSet rs) throws SQLException {
        LearningSession ls = new LearningSession();
        ls.setSessionId(rs.getInt("session_id"));
        ls.setRequestId(rs.getInt("request_id"));
        ls.setTeacherId(rs.getInt("teacher_id"));
        ls.setLearnerId(rs.getInt("learner_id"));
        ls.setSkillId(rs.getInt("skill_id"));
        ls.setSessionDate(rs.getDate("session_date"));
        ls.setStartTime(rs.getTime("start_time"));
        ls.setEndTime(rs.getTime("end_time"));
        ls.setMeetingMode(rs.getString("meeting_mode"));
        ls.setMeetingDetails(rs.getString("meeting_details"));
        ls.setStatus(rs.getString("status"));
        ls.setTeacherConfirmed(rs.getBoolean("teacher_confirmed"));
        ls.setLearnerConfirmed(rs.getBoolean("learner_confirmed"));
        ls.setCreatedAt(rs.getTimestamp("created_at"));

        ls.setTeacherName(rs.getString("teacher_name"));
        ls.setTeacherEmail(rs.getString("teacher_email"));
        ls.setTeacherDepartment(rs.getString("teacher_dept"));

        ls.setLearnerName(rs.getString("learner_name"));
        ls.setLearnerEmail(rs.getString("learner_email"));
        ls.setLearnerDepartment(rs.getString("learner_dept"));

        ls.setSkillName(rs.getString("skill_name"));
        return ls;
    }

    private static final String BASE_SELECT =
        "SELECT ls.*, " +
        "u1.full_name AS teacher_name, u1.email AS teacher_email, u1.department AS teacher_dept, " +
        "u2.full_name AS learner_name, u2.email AS learner_email, u2.department AS learner_dept, " +
        "s.skill_name " +
        "FROM learning_sessions ls " +
        "JOIN users u1 ON ls.teacher_id = u1.user_id " +
        "JOIN users u2 ON ls.learner_id = u2.user_id " +
        "JOIN skills s ON ls.skill_id = s.skill_id ";

    public int createSession(LearningSession session) {
        String sql = "INSERT INTO learning_sessions (request_id, teacher_id, learner_id, skill_id, " +
                     "session_date, start_time, end_time, meeting_mode, meeting_details, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, session.getRequestId());
            ps.setInt(2, session.getTeacherId());
            ps.setInt(3, session.getLearnerId());
            ps.setInt(4, session.getSkillId());
            ps.setDate(5, session.getSessionDate());
            ps.setTime(6, session.getStartTime());
            ps.setTime(7, session.getEndTime());
            ps.setString(8, session.getMeetingMode() != null ? session.getMeetingMode() : "ONLINE");
            ps.setString(9, session.getMeetingDetails());
            ps.setString(10, session.getStatus() != null ? session.getStatus() : "SCHEDULED");
            int rows = ps.executeUpdate();
            if (rows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating session", e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return -1;
    }

    public LearningSession findById(int sessionId) {
        String sql = BASE_SELECT + "WHERE ls.session_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sessionId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapSession(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding session by ID: " + sessionId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return null;
    }

    public List<LearningSession> findByUserId(int userId) {
        List<LearningSession> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE ls.teacher_id = ? OR ls.learner_id = ? ORDER BY ls.session_date DESC, ls.start_time DESC";
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
                list.add(mapSession(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding sessions for user: " + userId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    public List<LearningSession> findUpcomingByUserId(int userId) {
        List<LearningSession> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE (ls.teacher_id = ? OR ls.learner_id = ?) AND ls.status = 'SCHEDULED' " +
                     "AND (ls.session_date > CURRENT_DATE OR (ls.session_date = CURRENT_DATE AND ls.end_time >= CURRENT_TIME)) " +
                     "ORDER BY ls.session_date ASC, ls.start_time ASC";
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
                list.add(mapSession(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding upcoming sessions for user: " + userId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    public List<LearningSession> findCompletedByUserId(int userId) {
        List<LearningSession> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE (ls.teacher_id = ? OR ls.learner_id = ?) AND ls.status = 'COMPLETED' " +
                     "ORDER BY ls.session_date DESC, ls.start_time DESC";
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
                list.add(mapSession(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding completed sessions for user: " + userId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    /**
     * Checks if a user already has an active scheduled session overlapping with the given date and time range.
     */
    public boolean hasConflictingSession(int userId, Date date, Time start, Time end, int excludeSessionId) {
        String sql = "SELECT COUNT(*) FROM learning_sessions " +
                     "WHERE (teacher_id = ? OR learner_id = ?) " +
                     "AND session_date = ? " +
                     "AND status = 'SCHEDULED' " +
                     "AND session_id != ? " +
                     "AND ((start_time < ? AND end_time > ?) OR (start_time >= ? AND start_time < ?))";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setDate(3, date);
            ps.setInt(4, excludeSessionId);
            ps.setTime(5, end);
            ps.setTime(6, start);
            ps.setTime(7, start);
            ps.setTime(8, end);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking conflicting session for user: " + userId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return false;
    }

    public boolean setConfirmation(int sessionId, boolean isTeacher, boolean confirmed) {
        String column = isTeacher ? "teacher_confirmed" : "learner_confirmed";
        String sql = "UPDATE learning_sessions SET " + column + " = ? WHERE session_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setBoolean(1, confirmed);
            ps.setInt(2, sessionId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error confirming session: " + sessionId, e);
        } finally {
            DBConnection.closeQuietly(null, ps, conn);
        }
        return false;
    }

    public boolean updateStatus(int sessionId, String status) {
        String sql = "UPDATE learning_sessions SET status = ? WHERE session_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, sessionId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating session status: " + sessionId, e);
        } finally {
            DBConnection.closeQuietly(null, ps, conn);
        }
        return false;
    }

    public List<LearningSession> findAllSessions() {
        List<LearningSession> list = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY ls.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapSession(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all sessions", e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    public int getCompletedSessionsCount() {
        String sql = "SELECT COUNT(*) FROM learning_sessions WHERE status = 'COMPLETED'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting completed sessions", e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return 0;
    }
}

