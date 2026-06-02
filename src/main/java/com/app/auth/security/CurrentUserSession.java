package com.app.auth.security;

import java.time.LocalDateTime;

public final class CurrentUserSession {
    private static Long currentUserId;
    private static String username;
    private static String role;
    private static String fullname;
    private static LocalDateTime loginTime;

    private CurrentUserSession() {
    }

    public static void login(Long userId, String usernameValue, String roleValue, String fullnameValue) {
        currentUserId = userId;
        username = usernameValue;
        role = roleValue;
        fullname = fullnameValue;
        loginTime = LocalDateTime.now();
    }

    public static Long getCurrentUserId() {
        if (currentUserId == null) {
            throw new IllegalStateException("Chưa có user đăng nhập");
        }
        return currentUserId;
    }

    public static String getUsername() {
        return username;
    }

    public static String getRole() {
        return role;
    }

    public static String getFullname() {
        return fullname;
    }

    public static LocalDateTime getLoginTime() {
        return loginTime;
    }

    public static boolean isLoggedIn() {
        return currentUserId != null;
    }

    public static boolean hasRole(String requiredRole) {
        return role != null && role.equalsIgnoreCase(requiredRole);
    }

    public static void logout() {
        currentUserId = null;
        username = null;
        role = null;
        fullname = null;
        loginTime = null;
    }
}