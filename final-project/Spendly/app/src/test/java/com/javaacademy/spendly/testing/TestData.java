package com.javaacademy.spendly.testing;

import com.javaacademy.spendly.domain.Category;
import com.javaacademy.spendly.domain.Expense;

import java.time.LocalDate;

/** Short factory for test expenses. */
public final class TestData {

    private TestData() { }

    public static Expense expense(long id, String title, long cents, Category category, String date) {
        return new Expense(id, title, cents, category, LocalDate.parse(date), "");
    }
}
