package com.skillswap.util;

import java.util.regex.Pattern;

/**
 * Validation utility for SRM UNIVERSITY AP student registrations,
 * inputs, emails, student IDs, and XSS sanitization.
 */
public class ValidationUtil {

    // SRM University AP Student ID format: AP followed by digits (standard: AP + 11 digits)
    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile("^AP[0-9]{9,13}$", Pattern.CASE_INSENSITIVE);
    
    // College email regex - supports @srmap.edu.in or valid academic email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    private static final Pattern SRMAP_EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@srmap\\.edu\\.in$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidStudentId(String studentId) {
        if (studentId == null) return false;
        return STUDENT_ID_PATTERN.matcher(studentId.trim()).matches();
    }

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isSrmApEmail(String email) {
        if (email == null) return false;
        return SRMAP_EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidYear(int year) {
        return year >= 1 && year <= 5;
    }

    /**
     * Basic HTML escaping to mitigate Cross-Site Scripting (XSS).
     */
    public static String escapeHtml(String input) {
        if (input == null) return null;
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
    }

    /**
     * Sanitizes strings for safe display or query parameters.
     */
    public static String sanitize(String input) {
        if (input == null) return "";
        return input.trim();
    }
}

