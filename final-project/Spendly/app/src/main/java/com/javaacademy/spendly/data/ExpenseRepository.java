package com.javaacademy.spendly.data;

import androidx.lifecycle.LiveData;

import com.javaacademy.spendly.domain.Expense;

import java.util.List;

/**
 * The single source of truth for expenses. The UI depends on this interface, not on Room,
 * so tests can substitute an in-memory fake (lesson 20).
 */
public interface ExpenseRepository {

    /** All expenses, newest first. Emits again after every change. */
    LiveData<List<Expense>> observeAll();

    /** One expense, or {@code null} if it doesn't exist (any more). */
    LiveData<Expense> observe(long id);

    /**
     * Inserts a new expense ({@code id == 0}) or replaces the one with the same id, on a background
     * thread. Saving a just-deleted expense restores it, which is how "Undo" works.
     */
    void save(Expense expense);

    /** Deletes an expense, on a background thread. */
    void delete(Expense expense);
}
