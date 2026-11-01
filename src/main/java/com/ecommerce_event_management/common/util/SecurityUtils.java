package com.ecommerce_event_management.common.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * SecurityUtils: Convenient helper to retrieve authenticated user details from SecurityContext.
 */
public final class SecurityUtils {

    private SecurityUtils() {}

    public static Optional<Authentication> getAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return Optional.of(auth);
    }

    public static boolean isAuthenticated() {
        return getAuthentication().isPresent();
    }

    public static String getCurrentUserEmail() {
        return getAuthentication()
                .map(Authentication::getName)
                .orElse("system");
    }

    public static Long getCurrentUserId() {
        // Can be customized to extract Long userId from JWT Principal or custom UserDetails
        return 1L; // System default fallback
    }
}
