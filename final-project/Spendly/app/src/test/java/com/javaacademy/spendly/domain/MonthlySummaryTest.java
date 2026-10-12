package com.javaacademy.spendly.domain;

import static com.javaacademy.spendly.testing.TestData.expense;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public class MonthlySummaryTest {

    private final List<Expense> expenses = List.of(
            expense(1, "Groceries", 4_000, Category.FOOD, "2026-03-02"),
            expense(2, "Bus", 1_000, Category.TRANSPORT, "2026-03-05"),
            expense(3, "Cinema", 1_000, Category.FUN, "2026-03-09"),
            expense(4, "Old", 9_999, Category.BILLS, "2026-02-28"));

    @Test
    public void totalsOnlyTheRequestedMonth() {
        MonthlySummary s = MonthlySummary.of(expenses, YearMonth.of(2026, 3));
        assertEquals(6_000, s.totalCents());
        assertEquals(3, s.count());
        assertEquals(Long.valueOf(4_000), s.byCategory().get(Category.FOOD));
        assertFalse(s.byCategory().containsKey(Category.BILLS));
        assertEquals(Optional.of(Category.FOOD), s.topCategory());
        assertEquals(4_000.0 / 6_000, s.share(Category.FOOD), 1e-9);
    }

    @Test
    public void tiesGoToTheCategoryDeclaredFirst() {
        MonthlySummary s = MonthlySummary.of(expenses.subList(1, 3), YearMonth.of(2026, 3));
        assertEquals(Optional.of(Category.TRANSPORT), s.topCategory());
    }

    @Test
    public void emptyMonth() {
        MonthlySummary s = MonthlySummary.of(expenses, YearMonth.of(2025, 1));
        assertEquals(0, s.totalCents());
        assertEquals(Optional.empty(), s.topCategory());
        assertEquals(0.0, s.share(Category.FOOD), 0);
    }

    @Test
    public void averagePerDayDependsOnWhetherTheMonthIsOver() {
        MonthlySummary march = MonthlySummary.of(expenses, YearMonth.of(2026, 3));
        assertEquals(600, march.averagePerDayCents(LocalDate.of(2026, 3, 10)));   // 10 days so far
        assertEquals(194, march.averagePerDayCents(LocalDate.of(2026, 5, 1)));    // 6000 / 31
        assertEquals(0, march.averagePerDayCents(LocalDate.of(2026, 2, 1)));       // future month
    }
}
