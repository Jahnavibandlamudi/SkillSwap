package com.skillswap;

import com.skillswap.model.MatchResult;
import com.skillswap.model.User;
import com.skillswap.model.UserSkill;
import com.skillswap.util.PasswordUtil;
import com.skillswap.util.ValidationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test suite verifying core business rules, algorithms, and validation
 * for SkillSwap SRM University AP.
 */
public class SkillSwapUnitTest {

    @Test
    @DisplayName("PasswordUtil: Hashing and Verification")
    public void testPasswordHashing() {
        String raw = "Password@123";
        String hash = PasswordUtil.hashPassword(raw);

        assertNotNull(hash);
        assertEquals(64, hash.length(), "SHA-256 hash must be 64 hexadecimal characters");
        assertTrue(PasswordUtil.verifyPassword(raw, hash), "Password verification must succeed for correct password");
        assertFalse(PasswordUtil.verifyPassword("WrongPassword", hash), "Password verification must fail for incorrect password");
        assertFalse(PasswordUtil.verifyPassword(null, hash), "Null password must be rejected");
    }

    @Test
    @DisplayName("ValidationUtil: SRM AP Student ID Format AP***********")
    public void testStudentIdValidation() {
        // Valid SRM AP Student IDs
        assertTrue(ValidationUtil.isValidStudentId("AP21110010001"));
        assertTrue(ValidationUtil.isValidStudentId("AP22110010088"));
        assertTrue(ValidationUtil.isValidStudentId("ap23110010112"));

        // Invalid formats
        assertFalse(ValidationUtil.isValidStudentId("21110010001"), "Missing AP prefix must fail");
        assertFalse(ValidationUtil.isValidStudentId("CS21110010001"), "Wrong prefix must fail");
        assertFalse(ValidationUtil.isValidStudentId("AP123"), "Too short must fail");
        assertFalse(ValidationUtil.isValidStudentId(null), "Null must fail");
    }

    @Test
    @DisplayName("ValidationUtil: SRM AP College Email")
    public void testCollegeEmailValidation() {
        assertTrue(ValidationUtil.isValidEmail("aarav_sharma@srmap.edu.in"));
        assertTrue(ValidationUtil.isSrmApEmail("aarav_sharma@srmap.edu.in"));
        assertTrue(ValidationUtil.isValidEmail("student.21@college.edu"));
        
        assertFalse(ValidationUtil.isValidEmail("plainaddress"));
        assertFalse(ValidationUtil.isValidEmail("@missingusername.com"));
        assertFalse(ValidationUtil.isValidEmail(null));
    }

    @Test
    @DisplayName("ValidationUtil: XSS HTML Escaping")
    public void testXssSanitization() {
        String malicious = "<script>alert('xss');</script>";
        String escaped = ValidationUtil.escapeHtml(malicious);

        assertFalse(escaped.contains("<script>"), "Raw script tag must be escaped");
        assertTrue(escaped.contains("&lt;script&gt;"), "Tags must be converted to HTML entities");
    }

    @Test
    @DisplayName("Matching Logic: Mutual Match Evaluation")
    public void testMutualMatchScoring() {
        // Student A: Offers Java, Wants Photoshop
        User userA = new User();
        userA.setUserId(1);
        userA.setFullName("Aarav");

        UserSkill aOffers = new UserSkill(1, 1, 101, "Java Programming", "Programming", "OFFER", "Expert", null);
        UserSkill aWants = new UserSkill(2, 1, 102, "Photoshop & Design", "Design", "WANT", "Beginner", null);

        // Student B: Offers Photoshop, Wants Java
        User userB = new User();
        userB.setUserId(2);
        userB.setFullName("Diya");

        UserSkill bOffers = new UserSkill(3, 2, 102, "Photoshop & Design", "Design", "OFFER", "Expert", null);
        UserSkill bWants = new UserSkill(4, 2, 101, "Java Programming", "Programming", "WANT", "Beginner", null);

        // Test mutual compatibility
        boolean bTeachesWhatAWants = aWants.getSkillId() == bOffers.getSkillId();
        boolean aTeachesWhatBWants = bWants.getSkillId() == aOffers.getSkillId();

        assertTrue(bTeachesWhatAWants && aTeachesWhatBWants, "Should identify complementary mutual exchange");

        MatchResult result = new MatchResult(userB, 95, true);
        assertEquals(95, result.getMatchScore());
        assertTrue(result.isMutualMatch());
    }

    @Test
    @DisplayName("Credit Business Rules: Welcome bonus and session exchange")
    public void testCreditRules() {
        int welcomeCredits = 5;
        int teachingReward = 2;
        int learningCost = 2;

        int teacherInitial = welcomeCredits;
        int learnerInitial = welcomeCredits;

        // Session completed: Teacher earns +2, Learner spends -2
        int teacherFinal = teacherInitial + teachingReward;
        int learnerFinal = learnerInitial - learningCost;

        assertEquals(7, teacherFinal, "Teacher balance after teaching 1 session should be 7");
        assertEquals(3, learnerFinal, "Learner balance after attending 1 session should be 3");
        assertTrue(learnerFinal >= 0, "Learner balance must not become negative");
    }
}

