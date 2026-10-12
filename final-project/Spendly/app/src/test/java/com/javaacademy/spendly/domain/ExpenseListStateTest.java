package com.javaacademy.spendly.domain;

import static com.javaacademy.spendly.testing.TestData.expense;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.time.YearMonth;
import java.util.List;

public class ExpenseListStateTest {

    private final List<Expense> all = List.of(
            expense(1, "Coffee beans", 1_200, Category.FOOD, "2026-03-01"),
            expense(2, "Taxi home", 2_500, Category.TRANSPORT, "2026-03-03"),
            expense(3, "Coffee", 350, Category.FOOD, "2026-03-03"),
            expense(4, "February rent", 90_000, Category.BILLS, "2026-02-01"));

    private static List<Long> ids(ExpenseListState s) {
        return s.items().stream().map(Expense::id).toList();
    }

    @Test
    public void showsTheMonthNewestFirst() {
        ExpenseListState s = ExpenseListState.build(all, YearMonth.of(2026, 3), ExpenseFilter.ALL);
        assertEquals(List.of(3L, 2L, 1L), ids(s));        // same day: higher id (newer) first
        assertEquals(4_050, s.summary().totalCents());
    }

    @Test
    public void filtersByQueryAndCategoryButSummaryStaysUnfiltered() {
        ExpenseFilter coffee = ExpenseFilter.ALL.withQuery("COFFEE");
        assertEquals(List.of(3L, 1L), ids(ExpenseListState.build(all, YearMonth.of(2026, 3), coffee)));

        ExpenseListState transport = ExpenseListState.build(all, YearMonth.of(2026, 3), ExpenseFilter.ALL.withCategory(Category.TRANSPORT));
        assertEquals(List.of(2L), ids(transport));
        assertEquals(4_050, transport.summary().totalCents());
    }

    @Test
    public void noMatchesIsEmpty() {
        ExpenseListState s = ExpenseListState.build(all, YearMonth.of(2026, 3), ExpenseFilter.ALL.withQuery("pizza"));
        assertTrue(s.isEmpty());
        assertTrue(s.filter().isActive());
    }
}
