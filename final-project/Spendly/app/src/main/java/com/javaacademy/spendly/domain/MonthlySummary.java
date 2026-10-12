package com.javaacademy.spendly.domain;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Totals for one month: overall, per category, and the biggest category. */
public record MonthlySummary(YearMonth month, long totalCents, int count, Map<Category, Long> byCategory) {

    public MonthlySummary {
        EnumMap<Category, Long> copy = new EnumMap<>(Category.class);
        copy.putAll(byCategory);
        byCategory = Collections.unmodifiableMap(copy);
    }

    /** Builds the summary for {@code month} from any list of expenses (other months are ignored). */
    public static MonthlySummary of(List<Expense> expenses, YearMonth month) {
        Map<Category, Long> totals = new EnumMap<>(Category.class);
        long total = 0;
        int count = 0;
        for (Expense e : expenses) {
            if (!e.month().equals(month)) continue;
            totals.merge(e.category(), e.amountCents(), Long::sum);
            total += e.amountCents();
            count++;
        }
        return new MonthlySummary(month, total, count, totals);
    }

    public Optional<Category> topCategory() {
        return byCategory.entrySet().stream()
                // biggest total wins; on a tie, the category declared first wins
                .max(Map.Entry.<Category, Long>comparingByValue()
                        .thenComparing(Map.Entry.<Category, Long>comparingByKey().reversed()))
                .map(Map.Entry::getKey);
    }

    /** Share of the month's total for {@code category}, between 0 and 1. */
    public double share(Category category) {
        return totalCents == 0 ? 0 : (double) byCategory.getOrDefault(category, 0L) / totalCents;
    }

    /**
     * Average spending per day. For the current month we divide by the days elapsed so far,
     * for past months by the length of the month, and future months have no average.
     */
    public long averagePerDayCents(LocalDate today) {
        YearMonth current = YearMonth.from(today);
        int days;
        if (month.equals(current)) days = today.getDayOfMonth();
        else if (month.isBefore(current)) days = month.lengthOfMonth();
        else return 0;
        return Math.round((double) totalCents / days);
    }
}
