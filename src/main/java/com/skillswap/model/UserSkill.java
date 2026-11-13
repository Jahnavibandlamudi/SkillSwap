package com.skillswap.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing a student's offered or wanted skill with proficiency level.
 */
public class UserSkill implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userSkillId;
    private int userId;
    private int skillId;
    private String skillName;
    private String category;
    private String skillType;    // 'OFFER' or 'WANT'
    private String proficiency;  // 'Beginner', 'Intermediate', 'Advanced', 'Expert'
    private Timestamp createdAt;

    public UserSkill() {}

    public UserSkill(int userSkillId, int userId, int skillId, String skillName, String category,
                     String skillType, String proficiency, Timestamp createdAt) {
        this.userSkillId = userSkillId;
        this.userId = userId;
        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
        this.skillType = skillType;
        this.proficiency = proficiency;
        this.createdAt = createdAt;
    }

    public int getUserSkillId() { return userSkillId; }
    public void setUserSkillId(int userSkillId) { this.userSkillId = userSkillId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getSkillId() { return skillId; }
    public void setSkillId(int skillId) { this.skillId = skillId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSkillType() { return skillType; }
    public void setSkillType(String skillType) { this.skillType = skillType; }

    public String getProficiency() { return proficiency; }
    public void setProficiency(String proficiency) { this.proficiency = proficiency; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public boolean isOffered() {
        return "OFFER".equalsIgnoreCase(skillType);
    }

    public boolean isWanted() {
        return "WANT".equalsIgnoreCase(skillType);
    }
}

