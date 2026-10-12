package com.javaacademy.spendly.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.OptionalLong;

/** Parsing and formatting of money amounts held as hundredths in a {@code long}. */
public final class Money {

    /** Upper limit for a single expense: 10 million. */
    public static final long MAX_CENTS = 10_000_000_00L;

    private Money() { }

    /**
     * Parses user input such as {@code "12"}, {@code "12.5"}, {@code "12,50"} or {@code " 1 234.99 "}
     * (spaces are ignored and a comma is accepted as the decimal separator).
     *
     * @return the amount in cents, or empty if the text is not a number with at most two decimals
     */
    public static OptionalLong parseCents(String input) {
        if (input == null) return OptionalLong.empty();
        String s = input.replace("\u00A0", "").replace(" ", "").replace(',', '.');
        if (s.isEmpty() || !s.matches("-?\\d+(\\.\\d{1,2})?")) return OptionalLong.empty();
        try {
            return OptionalLong.of(new BigDecimal(s).movePointRight(2).longValueExact());
        } catch (ArithmeticException e) {
            return OptionalLong.empty();       // too large for a long
        }
    }

    /** Formats for an edit field: {@code 1250 → "12.50"} (no grouping, always a dot). */
    public static String toPlainString(long cents) {
        return BigDecimal.valueOf(cents, 2).toPlainString();
    }

    /** Formats for display: {@code 1250, "EUR", Locale.GERMANY → "12,50 €"}. */
    public static String format(long cents, String currencyCode, Locale locale) {
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        Currency currency = Currency.getInstance(currencyCode);
        format.setCurrency(currency);
        int digits = Math.max(0, currency.getDefaultFractionDigits());
        format.setMinimumFractionDigits(digits);
        format.setMaximumFractionDigits(digits);
        return format.format(BigDecimal.valueOf(cents, 2).setScale(digits, RoundingMode.HALF_EVEN));
    }

    /** Converts with an exchange rate, rounding half-even like banks do. */
    public static long convert(long cents, double rate) {
        if (rate <= 0 || Double.isNaN(rate) || Double.isInfinite(rate)) {
            throw new IllegalArgumentException("Invalid rate: " + rate);
        }
        return BigDecimal.valueOf(cents).multiply(BigDecimal.valueOf(rate)).setScale(0, RoundingMode.HALF_EVEN).longValueExact();
    }
}
