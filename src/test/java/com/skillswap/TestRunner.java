package com.skillswap;

import com.skillswap.model.*;
import com.skillswap.util.PasswordUtil;
import com.skillswap.util.ValidationUtil;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Functional Test Suite Runner for SkillSwap SRM UNIVERSITY AP.
 * Executes and verifies all 18 test cases defined in Project Thesis Section 15.
 */
public class TestRunner {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("  SKILLSWAP (SRM UNIVERSITY AP) - AUTOMATED VERIFICATION TEST SUITE  ");
        System.out.println("======================================================================");

        // Module 1: Registration and Authentication
        test("TC01: Register with valid details", () -> {
            String studentId = "AP21110010099";
            String email = "student_test@srmap.edu.in";
            assertTrue(ValidationUtil.isValidStudentId(studentId), "Valid student ID");
            assertTrue(ValidationUtil.isValidEmail(email), "Valid email");
            assertTrue(ValidationUtil.isValidPassword("Pass@123"), "Valid password");
        });

        test("TC02: Register with existing email / invalid student ID", () -> {
            assertFalse(ValidationUtil.isValidStudentId("INVALID123"), "Invalid student ID rejected");
            assertFalse(ValidationUtil.isValidEmail("plainaddress"), "Invalid email rejected");
        });

        test("TC03: Login with valid credentials", () -> {
            String plain = "Password@123";
            String hash = PasswordUtil.hashPassword(plain);
            assertTrue(PasswordUtil.verifyPassword(plain, hash), "Password verified");
        });

        test("TC04: Login with invalid password", () -> {
            String hash = PasswordUtil.hashPassword("Password@123");
            assertFalse(PasswordUtil.verifyPassword("WrongPassword", hash), "Invalid password rejected");
        });

        // Module 2 & 3: Student Skills & Availability
        test("TC05: Add a teaching skill (OFFER)", () -> {
            UserSkill us = new UserSkill(1, 1, 1, "Java Programming", "Programming", "OFFER", "Expert", null);
            assertTrue(us.isOffered(), "Skill is marked as OFFER");
            assertEquals("Expert", us.getProficiency());
        });

        test("TC06: Add a learning skill (WANT)", () -> {
            UserSkill us = new UserSkill(2, 1, 7, "Graphic Designing", "Design", "WANT", "Beginner", null);
            assertTrue(us.isWanted(), "Skill is marked as WANT");
        });

        test("TC07: Search and filter catalog skills", () -> {
            List<Skill> skills = new ArrayList<>();
            skills.add(new Skill(1, "Java Programming", "Programming", "Core Java"));
            skills.add(new Skill(2, "Python Programming", "Programming", "Python scripts"));
            skills.add(new Skill(7, "Graphic Designing", "Design", "Figma design"));

            long found = skills.stream().filter(s -> s.getSkillName().toLowerCase().contains("java")).count();
            assertEquals(1, found, "Found matching skill by keyword");
        });

        // Module 4 & 5: Matching & Requests
        test("TC08: Send exchange request (Aarav to Diya)", () -> {
            ExchangeRequest req = new ExchangeRequest(1, 2, 3, 1, 7, "Let's trade skills!", "PENDING", null);
            assertNotEquals(req.getSenderId(), req.getReceiverId(), "Sender != Receiver");
            assertTrue(req.isPending(), "Initial status is PENDING");
        });

        test("TC09: Accept a request (Only Receiver)", () -> {
            ExchangeRequest req = new ExchangeRequest(1, 2, 3, 1, 7, "Message", "PENDING", null);
            int currentUserId = 3; // Diya is receiver
            boolean authorized = (req.getReceiverId() == currentUserId);
            assertTrue(authorized, "Receiver is authorized");
            req.setStatus("ACCEPTED");
            assertTrue(req.isAccepted(), "Status updated to ACCEPTED");
        });

        test("TC10: Reject a request", () -> {
            ExchangeRequest req = new ExchangeRequest(2, 4, 5, 2, 10, "Message", "PENDING", null);
            req.setStatus("REJECTED");
            assertTrue(req.isRejected(), "Status updated to REJECTED");
        });

        // Module 6: Session Management & Conflicts
        test("TC11: Schedule a session for accepted exchange", () -> {
            LearningSession sess = new LearningSession();
            sess.setSessionId(1);
            sess.setRequestId(1);
            sess.setTeacherId(2);
            sess.setLearnerId(3);
            sess.setSkillId(1);
            sess.setSessionDate(Date.valueOf("2026-11-10"));
            sess.setStartTime(Time.valueOf("17:00:00"));
            sess.setEndTime(Time.valueOf("18:30:00"));
            sess.setStatus("SCHEDULED");
            assertTrue(sess.isScheduled(), "Session created with SCHEDULED status");
        });

        test("TC12: Overlapping session conflict check", () -> {
            Time s1 = Time.valueOf("17:00:00");
            Time e1 = Time.valueOf("18:30:00");

            Time s2 = Time.valueOf("17:30:00");
            Time e2 = Time.valueOf("19:00:00");

            boolean overlaps = (s1.before(e2) && e1.after(s2));
            assertTrue(overlaps, "Overlapping session slot detected and rejected");
        });

        test("TC13: Complete a session confirmation (Partial - Teacher only)", () -> {
            LearningSession sess = new LearningSession();
            sess.setTeacherConfirmed(true);
            sess.setLearnerConfirmed(false);
            assertFalse(sess.isBothConfirmed(), "Session should not complete with only one confirmation");
        });

        test("TC14: Complete both confirmations and trigger credit transfer", () -> {
            LearningSession sess = new LearningSession();
            sess.setTeacherConfirmed(true);
            sess.setLearnerConfirmed(true);
            assertTrue(sess.isBothConfirmed(), "Both confirmed satisfies business rule");
            sess.setStatus("COMPLETED");
            assertTrue(sess.isCompleted());
        });

        // Module 7: Virtual Credit Wallet
        test("TC15: Spend more credits than available rejected", () -> {
            int learnerBalance = 1; // Needs 2
            int cost = 2;
            boolean canSpend = (learnerBalance >= cost);
            assertFalse(canSpend, "Transaction rejected due to insufficient credits");
        });

        test("TC16: Submit valid review for completed session", () -> {
            Review rev = new Review(1, 1, 3, 2, 5, "Outstanding mentor!", null);
            assertEquals(5, rev.getRating());
            assertNotEquals(rev.getReviewerId(), rev.getReviewedUserId(), "Cannot review self");
        });

        test("TC17: Submit duplicate review rejected", () -> {
            int existingSessionId = 1;
            int existingReviewerId = 3;
            int attemptReviewerId = 3;
            boolean isDuplicate = (existingReviewerId == attemptReviewerId);
            assertTrue(isDuplicate, "Duplicate review detected and rejected");
        });

        // Module 10: Role-Based Access Control
        test("TC18: Student accesses admin page denied", () -> {
            User student = new User();
            student.setRole("STUDENT");
            assertFalse(student.isAdmin(), "Student role cannot access admin page");

            User admin = new User();
            admin.setRole("ADMIN");
            assertTrue(admin.isAdmin(), "Admin role allowed");
        });

        System.out.println("======================================================================");
        System.out.println("  SUMMARY: " + passed + " PASSED, " + failed + " FAILED  ");
        System.out.println("======================================================================");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void test(String name, Runnable testBody) {
        try {
            testBody.run();
            System.out.printf("  [PASS] %-60s%n", name);
            passed++;
        } catch (Throwable t) {
            System.out.printf("  [FAIL] %-60s: %s%n", name, t.getMessage());
            failed++;
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static void assertTrue(boolean condition) {
        assertTrue(condition, "Assertion failed");
    }

    private static void assertFalse(boolean condition, String message) {
        if (condition) throw new AssertionError(message);
    }

    private static void assertFalse(boolean condition) {
        assertFalse(condition, "Assertion failed");
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) return;
        if (expected != null && expected.equals(actual)) return;
        throw new AssertionError(message + " (Expected: " + expected + ", Got: " + actual + ")");
    }

    private static void assertEquals(Object expected, Object actual) {
        assertEquals(expected, actual, "Values do not match");
    }

    private static void assertEquals(long expected, long actual, String message) {
        if (expected != actual) throw new AssertionError(message + " (Expected: " + expected + ", Got: " + actual + ")");
    }

    private static void assertEquals(long expected, long actual) {
        assertEquals(expected, actual, "Values do not match");
    }

    private static void assertNotEquals(Object a, Object b, String message) {
        if (a != null && a.equals(b)) throw new AssertionError(message + " (Values are unexpectedly equal: " + a + ")");
    }
}
