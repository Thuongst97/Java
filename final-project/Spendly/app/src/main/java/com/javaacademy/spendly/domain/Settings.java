package com.javaacademy.spendly.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** User preferences. Immutable; the settings store hands out a new instance on every change. */
public record Settings(String currency, String convertTo, ThemeMode theme, boolean dailyReminder) {

    /** Currencies offered in the settings screen (ISO 4217 codes). */
    public static final List<String> CURRENCIES = Collections.unmodifiableList(Arrays.asList(
            "USD", "EUR", "GBP", "JPY", "VND", "INR", "AUD", "CAD", "CHF", "CNY", "SGD"));

    public static final Settings DEFAULT = new Settings("USD", "EUR", ThemeMode.SYSTEM, false);

    public Settings {
        Objects.requireNonNull(currency, "currency");
        Objects.requireNonNull(convertTo, "convertTo");
        Objects.requireNonNull(theme, "theme");
    }

    public Settings withCurrency(String code) { return new Settings(code, convertTo, theme, dailyReminder); }
    public Settings withConvertTo(String code) { return new Settings(currency, code, theme, dailyReminder); }
    public Settings withTheme(ThemeMode mode) { return new Settings(currency, convertTo, mode, dailyReminder); }
    public Settings withDailyReminder(boolean on) { return new Settings(currency, convertTo, theme, on); }
}
