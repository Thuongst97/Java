package com.javaacademy.spendly.domain;

/**
 * The fixed set of spending categories. Persisted by {@link #name()}, never by ordinal,
 * so constants can be reordered safely (lesson 09).
 */
public enum Category {
    FOOD("🍔", 0xFFE57373),
    TRANSPORT("🚌", 0xFF64B5F6),
    SHOPPING("🛍️", 0xFFBA68C8),
    BILLS("🧾", 0xFFFFB74D),
    FUN("🎉", 0xFF81C784),
    HEALTH("💊", 0xFF4DB6AC),
    OTHER("📦", 0xFF90A4AE);

    private final String emoji;
    private final int color;

    Category(String emoji, int color) {
        this.emoji = emoji;
        this.color = color;
    }

    public String emoji() {
        return emoji;
    }

    /** ARGB colour used for the category's chip and summary bar. */
    public int color() {
        return color;
    }

    /** "FOOD" → "Food". */
    public String displayName() {
        String n = name();
        return n.charAt(0) + n.substring(1).toLowerCase(java.util.Locale.ROOT);
    }

    /** Parses a stored name, falling back to {@link #OTHER} for unknown or missing values. */
    public static Category fromName(String name) {
        if (name == null) return OTHER;
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }
}
