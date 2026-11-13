package com.skillswap.model;

import java.io.Serializable;

/**
 * Model representing a categorized skill.
 */
public class Skill implements Serializable {
    private static final long serialVersionUID = 1L;

    private int skillId;
    private String skillName;
    private String category;
    private String description;

    // Transient analytics counts
    private int offeredCount;
    private int wantedCount;

    public Skill() {}

    public Skill(int skillId, String skillName, String category, String description) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
        this.description = description;
    }

    public int getSkillId() { return skillId; }
    public void setSkillId(int skillId) { this.skillId = skillId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getOfferedCount() { return offeredCount; }
    public void setOfferedCount(int offeredCount) { this.offeredCount = offeredCount; }

    public int getWantedCount() { return wantedCount; }
    public void setWantedCount(int wantedCount) { this.wantedCount = wantedCount; }
}

