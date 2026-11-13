package com.skillswap.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO representing an intelligent skill match between two students.
 */
public class MatchResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private User matchedUser;
    private int matchScore; // 0 to 100%
    private boolean mutualMatch; // True if A offers what B wants AND B offers what A wants
    private List<UserSkill> theyOfferWeWant = new ArrayList<>();
    private List<UserSkill> weOfferTheyWant = new ArrayList<>();
    private List<String> overlappingDays = new ArrayList<>();
    private String matchSummary;

    public MatchResult() {}

    public MatchResult(User matchedUser, int matchScore, boolean mutualMatch) {
        this.matchedUser = matchedUser;
        this.matchScore = matchScore;
        this.mutualMatch = mutualMatch;
    }

    public User getMatchedUser() { return matchedUser; }
    public void setMatchedUser(User matchedUser) { this.matchedUser = matchedUser; }

    public int getMatchScore() { return matchScore; }
    public void setMatchScore(int matchScore) { this.matchScore = matchScore; }

    public boolean isMutualMatch() { return mutualMatch; }
    public void setMutualMatch(boolean mutualMatch) { this.mutualMatch = mutualMatch; }

    public List<UserSkill> getTheyOfferWeWant() { return theyOfferWeWant; }
    public void setTheyOfferWeWant(List<UserSkill> theyOfferWeWant) { this.theyOfferWeWant = theyOfferWeWant; }

    public List<UserSkill> getWeOfferTheyWant() { return weOfferTheyWant; }
    public void setWeOfferTheyWant(List<UserSkill> weOfferTheyWant) { this.weOfferTheyWant = weOfferTheyWant; }

    public List<String> getOverlappingDays() { return overlappingDays; }
    public void setOverlappingDays(List<String> overlappingDays) { this.overlappingDays = overlappingDays; }

    public String getMatchSummary() { return matchSummary; }
    public void setMatchSummary(String matchSummary) { this.matchSummary = matchSummary; }
}

