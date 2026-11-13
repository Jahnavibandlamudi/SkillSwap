package com.skillswap.service;

import com.skillswap.dao.ReviewDAO;
import com.skillswap.dao.SessionDAO;
import com.skillswap.dao.SkillDAO;
import com.skillswap.dao.UserDAO;
import com.skillswap.model.MatchResult;
import com.skillswap.model.User;
import com.skillswap.model.UserAvailability;
import com.skillswap.model.UserSkill;
import com.skillswap.util.ValidationUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Intelligent Skill Matching Service for SRM UNIVERSITY AP students.
 * Implements Module 4 multi-factor scoring (Mutual match, availability overlap, proficiency suitability, profile completeness).
 */
public class MatchService {

    private final UserDAO userDAO = new UserDAO();
    private final SkillDAO skillDAO = new SkillDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();
    private final SessionDAO sessionDAO = new SessionDAO();

    // Configurable scoring weights
    private static final int WEIGHT_MUTUAL_SKILL = 60;
    private static final int WEIGHT_ONEWAY_SKILL = 30;
    private static final int WEIGHT_AVAILABILITY = 20;
    private static final int WEIGHT_PROFICIENCY = 10;
    private static final int WEIGHT_COMPLETENESS = 10;

    /**
     * Computes skill exchange recommendations for the given student.
     */
    public List<MatchResult> findMatches(int currentUserId, String categoryFilter, boolean mutualOnly, String searchQuery) {
        List<MatchResult> results = new ArrayList<>();

        // 1. Current student's skills and availability
        List<UserSkill> myOffered = skillDAO.getUserSkillsByType(currentUserId, "OFFER");
        List<UserSkill> myWanted = skillDAO.getUserSkillsByType(currentUserId, "WANT");
        List<UserAvailability> myAvail = skillDAO.getUserAvailability(currentUserId);

        Set<Integer> myOfferedSkillIds = myOffered.stream().map(UserSkill::getSkillId).collect(Collectors.toSet());
        Set<Integer> myWantedSkillIds = myWanted.stream().map(UserSkill::getSkillId).collect(Collectors.toSet());
        Set<String> myDays = myAvail.stream().map(UserAvailability::getDayOfWeek).collect(Collectors.toSet());

        // 2. All other active students
        List<User> allStudents = userDAO.findAllStudents();

        for (User candidate : allStudents) {
            if (candidate.getUserId() == currentUserId || !candidate.isActive()) {
                continue;
            }

            // Search query filter
            if (ValidationUtil.isNotEmpty(searchQuery)) {
                String q = searchQuery.toLowerCase().trim();
                boolean matchesName = candidate.getFullName().toLowerCase().contains(q);
                boolean matchesDept = candidate.getDepartment().toLowerCase().contains(q);
                boolean matchesId = candidate.getStudentId().toLowerCase().contains(q);
                if (!matchesName && !matchesDept && !matchesId) {
                    continue;
                }
            }

            List<UserSkill> candidateOffered = skillDAO.getUserSkillsByType(candidate.getUserId(), "OFFER");
            List<UserSkill> candidateWanted = skillDAO.getUserSkillsByType(candidate.getUserId(), "WANT");
            List<UserAvailability> candidateAvail = skillDAO.getUserAvailability(candidate.getUserId());

            // Check what candidate offers that current user wants
            List<UserSkill> theyOfferWeWant = new ArrayList<>();
            for (UserSkill cOffer : candidateOffered) {
                if (myWantedSkillIds.contains(cOffer.getSkillId())) {
                    if (ValidationUtil.isNotEmpty(categoryFilter) && !cOffer.getCategory().equalsIgnoreCase(categoryFilter)) {
                        continue;
                    }
                    theyOfferWeWant.add(cOffer);
                }
            }

            // Check what current user offers that candidate wants
            List<UserSkill> weOfferTheyWant = new ArrayList<>();
            for (UserSkill cWant : candidateWanted) {
                if (myOfferedSkillIds.contains(cWant.getSkillId())) {
                    weOfferTheyWant.add(cWant);
                }
            }

            // If there is no common learning match in either direction, skip
            if (theyOfferWeWant.isEmpty() && weOfferTheyWant.isEmpty()) {
                continue;
            }

            // Determine if it's a mutual match
            boolean isMutual = !theyOfferWeWant.isEmpty() && !weOfferTheyWant.isEmpty();

            if (mutualOnly && !isMutual) {
                continue;
            }

            // Overlapping availability days
            List<String> overlappingDays = new ArrayList<>();
            for (UserAvailability ca : candidateAvail) {
                if (myDays.contains(ca.getDayOfWeek()) && !overlappingDays.contains(ca.getDayOfWeek())) {
                    overlappingDays.add(ca.getDayOfWeek());
                }
            }

            // Calculate multi-factor match score (0-100)
            int score = 0;

            // 1. Skill compatibility (up to 60 pts)
            if (isMutual) {
                score += WEIGHT_MUTUAL_SKILL;
            } else {
                score += WEIGHT_ONEWAY_SKILL;
            }

            // 2. Availability compatibility (up to 20 pts)
            if (!overlappingDays.isEmpty()) {
                int overlapPoints = Math.min(WEIGHT_AVAILABILITY, overlappingDays.size() * 10);
                score += overlapPoints;
            }

            // 3. Proficiency suitability (up to 10 pts)
            int bestProficiencyScore = 0;
            for (UserSkill us : theyOfferWeWant) {
                String prof = us.getProficiency();
                if ("Expert".equalsIgnoreCase(prof)) bestProficiencyScore = Math.max(bestProficiencyScore, 10);
                else if ("Advanced".equalsIgnoreCase(prof)) bestProficiencyScore = Math.max(bestProficiencyScore, 8);
                else if ("Intermediate".equalsIgnoreCase(prof)) bestProficiencyScore = Math.max(bestProficiencyScore, 6);
                else bestProficiencyScore = Math.max(bestProficiencyScore, 4);
            }
            score += bestProficiencyScore;

            // 4. Profile completeness & reputation (up to 10 pts)
            int completeness = 0;
            if (ValidationUtil.isNotEmpty(candidate.getBio()) && candidate.getBio().length() > 20) completeness += 4;
            if (ValidationUtil.isNotEmpty(candidate.getDepartment())) completeness += 3;
            double rating = reviewDAO.getAverageRating(candidate.getUserId());
            if (rating > 0.0) completeness += 3;
            score += completeness;

            score = Math.min(100, Math.max(10, score));

            // Enrich candidate details
            candidate.setAverageRating(rating);
            candidate.setCompletedSessionsCount(sessionDAO.findCompletedByUserId(candidate.getUserId()).size());

            MatchResult mr = new MatchResult(candidate, score, isMutual);
            mr.setTheyOfferWeWant(theyOfferWeWant);
            mr.setWeOfferTheyWant(weOfferTheyWant);
            mr.setOverlappingDays(overlappingDays);

            if (isMutual) {
                mr.setMatchSummary("Mutual Match: You can teach each other complementary skills!");
            } else if (!theyOfferWeWant.isEmpty()) {
                mr.setMatchSummary("Learning Opportunity: " + candidate.getFullName() + " offers skills you want to learn.");
            } else {
                mr.setMatchSummary("Mentorship Opportunity: You can teach skills " + candidate.getFullName() + " wants to learn.");
            }

            results.add(mr);
        }

        // Sort results: Mutual matches first, then highest match score, then highest rating
        results.sort((a, b) -> {
            if (a.isMutualMatch() != b.isMutualMatch()) {
                return a.isMutualMatch() ? -1 : 1;
            }
            if (a.getMatchScore() != b.getMatchScore()) {
                return Integer.compare(b.getMatchScore(), a.getMatchScore());
            }
            return Double.compare(b.getMatchedUser().getAverageRating(), a.getMatchedUser().getAverageRating());
        });

        return results;
    }
}

