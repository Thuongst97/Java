package com.javaacademy.spendly.domain;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Everything the list screen shows, computed from all expenses + the selected month + the filter.
 * A pure function of its inputs, so it's trivial to unit-test (lesson 19: unidirectional data flow).
 *
 * @param items   the month's expenses that match the filter, newest first
 * @param summary totals for the whole month (unfiltered)
 */
public record ExpenseListState(YearMonth month, ExpenseFilter filter, List<Expense> items, MonthlySummary summary) {

    private static final Comparator<Expense> NEWEST_FIRST =
            Comparator.comparing(Expense::date).thenComparingLong(Expense::id).reversed();

    public ExpenseListState {
        items = Collections.unmodifiableList(new ArrayList<>(items));
    }

    public static ExpenseListState build(List<Expense> all, YearMonth month, ExpenseFilter filter) {
        List<Expense> items = all.stream()
                .filter(e -> e.month().equals(month))
                .filter(filter::matches)
                .sorted(NEWEST_FIRST)
                .collect(Collectors.toList());
        return new ExpenseListState(month, filter, items, MonthlySummary.of(all, month));
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
