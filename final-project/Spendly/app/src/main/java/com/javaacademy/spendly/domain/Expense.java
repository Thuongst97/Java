package com.javaacademy.spendly.domain;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

/**
 * One expense. Amounts are stored as whole hundredths ("cents") in a {@code long} — never as a
 * {@code double} (lesson 03). An {@code id} of {@link #NEW_ID} means "not saved yet".
 */
public record Expense(long id, String title, long amountCents, Category category, LocalDate date, String note) {

    public static final long NEW_ID = 0;

    public Expense {
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(category, "category");
        Objects.requireNonNull(date, "date");
        if (amountCents <= 0) throw new IllegalArgumentException("amountCents must be > 0");
        title = title.trim();
        note = note == null ? "" : note.trim();
    }

    public boolean isNew() {
        return id == NEW_ID;
    }

    public YearMonth month() {
        return YearMonth.from(date);
    }

    public Expense withId(long newId) {
        return new Expense(newId, title, amountCents, category, date, note);
    }
}
