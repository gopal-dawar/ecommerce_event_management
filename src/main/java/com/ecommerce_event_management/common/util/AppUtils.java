package com.ecommerce_event_management.common.util;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * AppUtils: Core utility functions for code generation, slug formatting, and text manipulation.
 */
public final class AppUtils {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AppUtils() {}

    /**
     * Generates a unique business code with a custom prefix, e.g. generateCode("ORD") -> ORD_20261101_A3F9
     */
    public static String generateCode(String prefix) {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return String.format("%s_%s_%s", prefix.toUpperCase(), datePart, randomPart);
    }

    /**
     * Converts a title or name to an SEO-friendly URL slug:
     * e.g. "Annual Cultural Fest 2026!" -> "annual-cultural-fest-2026"
     */
    public static String slugify(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }
        String nowhitespace = WHITESPACE.matcher(input.trim()).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH).replaceAll("-+", "-").replaceAll("^-|-$", "");
    }

    /**
     * Formats LocalDateTime to standard string representation
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DEFAULT_FORMATTER);
    }

    /**
     * Safely checks if a string is non-null and non-empty
     */
    public static boolean hasText(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
