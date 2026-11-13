package com.skillswap.dao;

import com.skillswap.model.Skill;
import com.skillswap.model.UserAvailability;
import com.skillswap.model.UserSkill;
import com.skillswap.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Skills, UserSkills, and UserAvailability.
 */
public class SkillDAO {

    private static final Logger LOGGER = Logger.getLogger(SkillDAO.class.getName());

    public List<Skill> findAllSkills() {
        List<Skill> list = new ArrayList<>();
        String sql = "SELECT s.*, " +
                     "(SELECT COUNT(*) FROM user_skills WHERE skill_id = s.skill_id AND skill_type = 'OFFER') AS offered_count, " +
                     "(SELECT COUNT(*) FROM user_skills WHERE skill_id = s.skill_id AND skill_type = 'WANT') AS wanted_count " +
                     "FROM skills s ORDER BY s.category, s.skill_name";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Skill s = new Skill();
                s.setSkillId(rs.getInt("skill_id"));
                s.setSkillName(rs.getString("skill_name"));
                s.setCategory(rs.getString("category"));
                s.setDescription(rs.getString("description"));
                s.setOfferedCount(rs.getInt("offered_count"));
                s.setWantedCount(rs.getInt("wanted_count"));
                list.add(s);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching skills", e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    public Skill findById(int skillId) {
        String sql = "SELECT * FROM skills WHERE skill_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, skillId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Skill(rs.getInt("skill_id"), rs.getString("skill_name"),
                                 rs.getString("category"), rs.getString("description"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding skill by ID: " + skillId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return null;
    }

    public List<String> findAllCategories() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM skills ORDER BY category";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching categories", e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    public int createSkill(Skill skill) {
        String sql = "INSERT INTO skills (skill_name, category, description) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, skill.getSkillName().trim());
            ps.setString(2, skill.getCategory().trim());
            ps.setString(3, skill.getDescription());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting skill: " + skill.getSkillName(), e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return -1;
    }

    public boolean deleteSkill(int skillId) {
        String sql = "DELETE FROM skills WHERE skill_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, skillId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting skill ID: " + skillId, e);
        } finally {
            DBConnection.closeQuietly(null, ps, conn);
        }
        return false;
    }

    public List<UserSkill> getUserSkills(int userId) {
        List<UserSkill> list = new ArrayList<>();
        String sql = "SELECT us.*, s.skill_name, s.category FROM user_skills us " +
                     "JOIN skills s ON us.skill_id = s.skill_id " +
                     "WHERE us.user_id = ? ORDER BY us.skill_type, s.skill_name";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                UserSkill us = new UserSkill();
                us.setUserSkillId(rs.getInt("user_skill_id"));
                us.setUserId(rs.getInt("user_id"));
                us.setSkillId(rs.getInt("skill_id"));
                us.setSkillName(rs.getString("skill_name"));
                us.setCategory(rs.getString("category"));
                us.setSkillType(rs.getString("skill_type"));
                us.setProficiency(rs.getString("proficiency"));
                us.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(us);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching user skills for userId: " + userId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    public List<UserSkill> getUserSkillsByType(int userId, String skillType) {
        List<UserSkill> list = new ArrayList<>();
        String sql = "SELECT us.*, s.skill_name, s.category FROM user_skills us " +
                     "JOIN skills s ON us.skill_id = s.skill_id " +
                     "WHERE us.user_id = ? AND us.skill_type = ? ORDER BY s.skill_name";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, skillType);
            rs = ps.executeQuery();
            while (rs.next()) {
                UserSkill us = new UserSkill();
                us.setUserSkillId(rs.getInt("user_skill_id"));
                us.setUserId(rs.getInt("user_id"));
                us.setSkillId(rs.getInt("skill_id"));
                us.setSkillName(rs.getString("skill_name"));
                us.setCategory(rs.getString("category"));
                us.setSkillType(rs.getString("skill_type"));
                us.setProficiency(rs.getString("proficiency"));
                us.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(us);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching user skills by type for userId: " + userId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    public boolean addUserSkill(int userId, int skillId, String skillType, String proficiency) {
        String sql = "INSERT INTO user_skills (user_id, skill_id, skill_type, proficiency) " +
                     "VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE proficiency = VALUES(proficiency)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, skillId);
            ps.setString(3, skillType);
            ps.setString(4, proficiency);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding user skill: " + skillId + " for user: " + userId, e);
        } finally {
            DBConnection.closeQuietly(null, ps, conn);
        }
        return false;
    }

    public boolean removeUserSkill(int userSkillId, int userId) {
        String sql = "DELETE FROM user_skills WHERE user_skill_id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userSkillId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing user skill: " + userSkillId, e);
        } finally {
            DBConnection.closeQuietly(null, ps, conn);
        }
        return false;
    }

    public List<UserAvailability> getUserAvailability(int userId) {
        List<UserAvailability> list = new ArrayList<>();
        String sql = "SELECT * FROM user_availability WHERE user_id = ? ORDER BY FIELD(day_of_week, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'), start_time";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new UserAvailability(
                    rs.getInt("availability_id"),
                    rs.getInt("user_id"),
                    rs.getString("day_of_week"),
                    rs.getTime("start_time"),
                    rs.getTime("end_time")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching user availability for user: " + userId, e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    public boolean addAvailabilitySlot(int userId, String dayOfWeek, Time startTime, Time endTime) {
        String sql = "INSERT INTO user_availability (user_id, day_of_week, start_time, end_time) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, dayOfWeek);
            ps.setTime(3, startTime);
            ps.setTime(4, endTime);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding availability for user: " + userId, e);
        } finally {
            DBConnection.closeQuietly(null, ps, conn);
        }
        return false;
    }

    public boolean removeAvailabilitySlot(int availabilityId, int userId) {
        String sql = "DELETE FROM user_availability WHERE availability_id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, availabilityId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing availability slot: " + availabilityId, e);
        } finally {
            DBConnection.closeQuietly(null, ps, conn);
        }
        return false;
    }

    public int getTotalSkillsCount() {
        String sql = "SELECT COUNT(*) FROM skills";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting skills", e);
        } finally {
            DBConnection.closeQuietly(rs, ps, conn);
        }
        return 0;
    }
}

