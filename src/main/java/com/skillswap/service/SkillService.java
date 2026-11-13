package com.skillswap.service;

import com.skillswap.dao.SkillDAO;
import com.skillswap.model.Skill;
import com.skillswap.model.UserAvailability;
import com.skillswap.model.UserSkill;
import com.skillswap.util.ValidationUtil;

import java.sql.Time;
import java.util.List;

/**
 * Service for catalog skills, student skills, and schedule availability.
 */
public class SkillService {

    private final SkillDAO skillDAO = new SkillDAO();

    public List<Skill> getAllSkills() {
        return skillDAO.findAllSkills();
    }

    public Skill getSkillById(int skillId) {
        return skillDAO.findById(skillId);
    }

    public List<String> getAllCategories() {
        return skillDAO.findAllCategories();
    }

    public List<UserSkill> getUserSkills(int userId) {
        return skillDAO.getUserSkills(userId);
    }

    public List<UserSkill> getUserOfferedSkills(int userId) {
        return skillDAO.getUserSkillsByType(userId, "OFFER");
    }

    public List<UserSkill> getUserWantedSkills(int userId) {
        return skillDAO.getUserSkillsByType(userId, "WANT");
    }

    public boolean addUserSkill(int userId, int skillId, String skillType, String proficiency) {
        if (skillId <= 0 || !ValidationUtil.isNotEmpty(skillType) || !ValidationUtil.isNotEmpty(proficiency)) {
            return false;
        }
        return skillDAO.addUserSkill(userId, skillId, skillType.toUpperCase().trim(), proficiency.trim());
    }

    public boolean removeUserSkill(int userSkillId, int userId) {
        return skillDAO.removeUserSkill(userSkillId, userId);
    }

    public List<UserAvailability> getUserAvailability(int userId) {
        return skillDAO.getUserAvailability(userId);
    }

    public boolean addAvailabilitySlot(int userId, String dayOfWeek, Time startTime, Time endTime) {
        if (!ValidationUtil.isNotEmpty(dayOfWeek) || startTime == null || endTime == null) {
            return false;
        }
        if (endTime.before(startTime) || endTime.equals(startTime)) {
            return false;
        }
        return skillDAO.addAvailabilitySlot(userId, dayOfWeek.trim(), startTime, endTime);
    }

    public boolean removeAvailabilitySlot(int availabilityId, int userId) {
        return skillDAO.removeAvailabilitySlot(availabilityId, userId);
    }

    public boolean createSkill(String name, String category, String description) {
        if (!ValidationUtil.isNotEmpty(name) || !ValidationUtil.isNotEmpty(category)) {
            return false;
        }
        Skill s = new Skill(0, ValidationUtil.sanitize(name), ValidationUtil.sanitize(category),
                            ValidationUtil.sanitize(description));
        return skillDAO.createSkill(s) > 0;
    }

    public boolean deleteSkill(int skillId) {
        return skillDAO.deleteSkill(skillId);
    }
}

