package com.skillswap.service;

import com.skillswap.dao.CreditDAO;
import com.skillswap.dao.NotificationDAO;
import com.skillswap.dao.ReviewDAO;
import com.skillswap.dao.SessionDAO;
import com.skillswap.dao.UserDAO;
import com.skillswap.model.Notification;
import com.skillswap.model.User;
import com.skillswap.util.PasswordUtil;
import com.skillswap.util.ValidationUtil;

/**
 * Service handling student registration, authentication, session data enrichment.
 */
public class AuthService {

    private final UserDAO userDAO = new UserDAO();
    private final CreditDAO creditDAO = new CreditDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();
    private final SessionDAO sessionDAO = new SessionDAO();

    public static class AuthResult {
        private final boolean success;
        private final String message;
        private final User user;

        public AuthResult(boolean success, String message, User user) {
            this.success = success;
            this.message = message;
            this.user = user;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public User getUser() { return user; }
    }

    /**
     * Registers a new student exclusively for SRM UNIVERSITY AP.
     */
    public AuthResult register(String studentId, String fullName, String email, String password,
                               String confirmPassword, String department, int yearOfStudy, String bio) {

        // 1. Mandatory fields validation
        if (!ValidationUtil.isNotEmpty(studentId) || !ValidationUtil.isNotEmpty(fullName) ||
            !ValidationUtil.isNotEmpty(email) || !ValidationUtil.isNotEmpty(password) ||
            !ValidationUtil.isNotEmpty(department)) {
            return new AuthResult(false, "All mandatory fields must be filled.", null);
        }

        // 2. Student ID format check: AP***********
        if (!ValidationUtil.isValidStudentId(studentId)) {
            return new AuthResult(false, "Invalid SRM AP Student ID format. Must begin with 'AP' followed by digits (e.g., AP21110010001).", null);
        }

        // 3. Email validation
        if (!ValidationUtil.isValidEmail(email)) {
            return new AuthResult(false, "Please provide a valid email address.", null);
        }

        // 4. Password validation
        if (!ValidationUtil.isValidPassword(password)) {
            return new AuthResult(false, "Password must be at least 6 characters in length.", null);
        }

        if (!password.equals(confirmPassword)) {
            return new AuthResult(false, "Password and confirm password do not match.", null);
        }

        // 5. Year validation
        if (!ValidationUtil.isValidYear(yearOfStudy)) {
            return new AuthResult(false, "Year of study must be between 1 and 5.", null);
        }

        // 6. Duplicate checks
        if (userDAO.findByEmail(email) != null) {
            return new AuthResult(false, "An account with email " + email + " is already registered.", null);
        }

        if (userDAO.findByStudentId(studentId) != null) {
            return new AuthResult(false, "An account with Student ID " + studentId + " is already registered.", null);
        }

        // 7. Hash password
        String hash = PasswordUtil.hashPassword(password);

        // 8. Create user
        User user = new User();
        user.setStudentId(studentId.toUpperCase().trim());
        user.setFullName(ValidationUtil.sanitize(fullName));
        user.setEmail(email.toLowerCase().trim());
        user.setPasswordHash(hash);
        user.setCollege("SRM UNIVERSITY AP");
        user.setDepartment(department.trim());
        user.setYearOfStudy(yearOfStudy);
        user.setBio(ValidationUtil.sanitize(bio));
        user.setProfileImage("default-avatar.png");
        user.setRole("STUDENT");
        user.setStatus("ACTIVE");

        int userId = userDAO.createUser(user);
        if (userId <= 0) {
            return new AuthResult(false, "An error occurred while creating your account. Please try again.", null);
        }
        user.setUserId(userId);

        // 9. Initialize credit account with 5 welcome credits
        creditDAO.createInitialAccount(userId, 5);

        // 10. Send welcome notification
        Notification welcomeNotif = new Notification();
        welcomeNotif.setUserId(userId);
        welcomeNotif.setMessage("Welcome to SkillSwap SRM AP! 5 welcome credits have been credited to your wallet.");
        welcomeNotif.setType("WALLET");
        welcomeNotif.setLink("credits");
        welcomeNotif.setRead(false);
        notificationDAO.createNotification(welcomeNotif);

        return new AuthResult(true, "Registration successful! Welcome to SkillSwap SRM AP.", user);
    }

    /**
     * Authenticates student or administrator with email or studentId.
     */
    public AuthResult login(String identifier, String password) {
        if (!ValidationUtil.isNotEmpty(identifier) || !ValidationUtil.isNotEmpty(password)) {
            return new AuthResult(false, "Please provide your email/Student ID and password.", null);
        }

        User user = null;
        if (identifier.contains("@")) {
            user = userDAO.findByEmail(identifier);
        } else {
            user = userDAO.findByStudentId(identifier);
        }

        if (user == null) {
            return new AuthResult(false, "Invalid credentials. No account found.", null);
        }

        if (!user.isActive()) {
            return new AuthResult(false, "Your account has been deactivated by the administrator.", null);
        }

        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            return new AuthResult(false, "Incorrect password. Please try again.", null);
        }

        // Enrich user with transient attributes
        enrichUserAttributes(user);

        return new AuthResult(true, "Login successful.", user);
    }

    public void enrichUserAttributes(User user) {
        if (user != null) {
            user.setCreditBalance(creditDAO.getBalance(user.getUserId()));
            user.setAverageRating(reviewDAO.getAverageRating(user.getUserId()));
            user.setCompletedSessionsCount(sessionDAO.findCompletedByUserId(user.getUserId()).size());
        }
    }
}

