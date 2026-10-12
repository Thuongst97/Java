package com.javaacademy.spendly.domain;

/** The user's theme preference. */
public enum ThemeMode {
    SYSTEM, LIGHT, DARK;

    public static ThemeMode fromName(String name) {
        for (ThemeMode m : values()) {
            if (m.name().equals(name)) return m;
        }
        return SYSTEM;
    }
}
