package com.javaacademy.spendly.domain;

import java.util.Locale;

/**
 * What the user is looking for on the list screen: a free-text query and an optional category
 * ({@code null} = all categories). Immutable; use the {@code with…} methods to change it.
 */
public record ExpenseFilter(String query, Category category) {

    public static final ExpenseFilter ALL = new ExpenseFilter("", null);

    public ExpenseFilter {
        query = query == null ? "" : query.trim();
    }

    public ExpenseFilter withQuery(String newQuery) {
        return new ExpenseFilter(newQuery, category);
    }

    public ExpenseFilter withCategory(Category newCategory) {
        return new ExpenseFilter(query, newCategory);
    }

    public boolean isActive() {
        return !query.isEmpty() || category != null;
    }

    public boolean matches(Expense e) {
        if (category != null && e.category() != category) return false;
        if (query.isEmpty()) return true;
        String q = query.toLowerCase(Locale.ROOT);
        return e.title().toLowerCase(Locale.ROOT).contains(q) || e.note().toLowerCase(Locale.ROOT).contains(q);
    }
}
