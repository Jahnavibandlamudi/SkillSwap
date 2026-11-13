package com.skillswap.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility for hashing and verifying student and administrator passwords.
 * Uses SHA-256 with application-level salting and constant-time equality check.
 */
public class PasswordUtil {

    private static final String SALT = "srmap_skillswap_salt";

    /**
     * Hashes a raw password with salt using SHA-256.
     * @param plainPassword Raw password string
     * @return Hexadecimal string representation of the hash
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String salted = SALT + plainPassword + SALT;
            byte[] hashBytes = digest.digest(salted.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Verifies whether the provided raw password matches the stored hash.
     * @param plainPassword Raw candidate password
     * @param storedHash Hashed password from database
     * @return true if matches, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) {
            return false;
        }
        String calculated = hashPassword(plainPassword);
        return MessageDigest.isEqual(calculated.getBytes(StandardCharsets.UTF_8),
                                     storedHash.getBytes(StandardCharsets.UTF_8));
    }
}

