package com.javaacademy.spendly.domain;

import java.time.LocalDate;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.OptionalLong;

/**
 * Turns raw form input into an {@link Expense}, or reports what is wrong with each field.
 * Knows nothing about Android: the UI maps each {@link Problem} to a localised string resource.
 */
public final class ExpenseValidator {

    public static final int MAX_TITLE_LENGTH = 60;

    public enum Field { TITLE, AMOUNT, CATEGORY, DATE }

    public enum Problem { REQUIRED, TOO_LONG, NOT_A_NUMBER, NOT_POSITIVE, TOO_LARGE, IN_THE_FUTURE }

    /** Either a valid expense or a map of field → problem. */
    public record Result(Expense expense, Map<Field, Problem> problems) {
        public Result {
            EnumMap<Field, Problem> copy = new EnumMap<>(Field.class);
            copy.putAll(problems);
            problems = Collections.unmodifiableMap(copy);
        }

        public boolean isValid() {
            return expense != null;
        }
    }

    private ExpenseValidator() { }

    public static Result validate(long id, String title, String amount, Category category,
                                  LocalDate date, String note, LocalDate today) {
        Map<Field, Problem> problems = new EnumMap<>(Field.class);

        String cleanTitle = title == null ? "" : title.trim();
        if (cleanTitle.isEmpty()) problems.put(Field.TITLE, Problem.REQUIRED);
        else if (cleanTitle.length() > MAX_TITLE_LENGTH) problems.put(Field.TITLE, Problem.TOO_LONG);

        long cents = 0;
        if (amount == null || amount.trim().isEmpty()) {
            problems.put(Field.AMOUNT, Problem.REQUIRED);
        } else {
            OptionalLong parsed = Money.parseCents(amount);
            if (parsed.isEmpty()) problems.put(Field.AMOUNT, Problem.NOT_A_NUMBER);
            else if (parsed.getAsLong() <= 0) problems.put(Field.AMOUNT, Problem.NOT_POSITIVE);
            else if (parsed.getAsLong() > Money.MAX_CENTS) problems.put(Field.AMOUNT, Problem.TOO_LARGE);
            else cents = parsed.getAsLong();
        }

        if (category == null) problems.put(Field.CATEGORY, Problem.REQUIRED);

        if (date == null) problems.put(Field.DATE, Problem.REQUIRED);
        else if (date.isAfter(today)) problems.put(Field.DATE, Problem.IN_THE_FUTURE);

        if (!problems.isEmpty()) return new Result(null, problems);
        return new Result(new Expense(id, cleanTitle, cents, category, date, note), Collections.emptyMap());
    }
}
