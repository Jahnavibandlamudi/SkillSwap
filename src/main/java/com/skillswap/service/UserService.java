package com.skillswap.service;

import com.skillswap.dao.CreditDAO;
import com.skillswap.dao.ReviewDAO;
import com.skillswap.dao.SessionDAO;
import com.skillswap.dao.SkillDAO;
import com.skillswap.dao.UserDAO;
import com.skillswap.model.User;
import com.skillswap.util.PasswordUtil;
import com.skillswap.util.ValidationUtil;

import java.util.List;

/**
 * Service for student profile management, searching, and updates.
 */
public class UserService {

    private final UserDAO userDAO = new UserDAO();
    private final CreditDAO creditDAO = new CreditDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();
    private final SessionDAO sessionDAO = new SessionDAO();
    private final SkillDAO skillDAO = new SkillDAO();

    public User getUserById(int userId) {
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setCreditBalance(creditDAO.getBalance(userId));
            user.setAverageRating(reviewDAO.getAverageRating(userId));
            user.setCompletedSessionsCount(sessionDAO.findCompletedByUserId(userId).size());
        }
        return user;
    }

    public boolean updateProfile(int userId, String fullName, String department, int yearOfStudy,
                                 String bio, String profileImage) {
        if (!ValidationUtil.isNotEmpty(fullName) || !ValidationUtil.isNotEmpty(department)) {
            return false;
        }
        User user = userDAO.findById(userId);
        if (user == null) {
            return false;
        }
        user.setFullName(ValidationUtil.sanitize(fullName));
        user.setDepartment(department.trim());
        user.setYearOfStudy(yearOfStudy);
        user.setBio(ValidationUtil.sanitize(bio));
        if (ValidationUtil.isNotEmpty(profileImage)) {
            user.setProfileImage(profileImage);
        }
        return userDAO.updateProfile(user);
    }

    public boolean changePassword(int userId, String currentPassword, String newPassword, String confirmPassword) {
        if (!ValidationUtil.isValidPassword(newPassword) || !newPassword.equals(confirmPassword)) {
            return false;
        }
        User user = userDAO.findById(userId);
        if (user == null) {
            return false;
        }
        if (!PasswordUtil.verifyPassword(currentPassword, user.getPasswordHash())) {
            return false;
        }
        String newHash = PasswordUtil.hashPassword(newPassword);
        return userDAO.updatePassword(userId, newHash);
    }

    public List<User> searchStudents(String query) {
        List<User> list = userDAO.searchStudents(query);
        for (User u : list) {
            u.setAverageRating(reviewDAO.getAverageRating(u.getUserId()));
            u.setCompletedSessionsCount(sessionDAO.findCompletedByUserId(u.getUserId()).size());
        }
        return list;
    }

    public List<User> getAllStudents() {
        List<User> list = userDAO.findAllStudents();
        for (User u : list) {
            u.setAverageRating(reviewDAO.getAverageRating(u.getUserId()));
            u.setCompletedSessionsCount(sessionDAO.findCompletedByUserId(u.getUserId()).size());
        }
        return list;
    }
}

