package com.javaacademy.spendly.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

/**
 * Data-access object. Room's annotation processor generates the implementation at build time.
 * Queries returning {@link LiveData} run on a background thread and re-emit whenever the table
 * changes; the other methods block, so callers must run them off the main thread.
 */
@Dao
public interface ExpenseDao {

    @Query("SELECT * FROM expenses ORDER BY date DESC, id DESC")
    LiveData<List<ExpenseEntity>> observeAll();

    @Query("SELECT * FROM expenses WHERE id = :id")
    LiveData<ExpenseEntity> observeById(long id);

    @Query("SELECT * FROM expenses WHERE date BETWEEN :fromEpochDay AND :toEpochDay ORDER BY date DESC, id DESC")
    List<ExpenseEntity> getBetween(long fromEpochDay, long toEpochDay);

    /** Inserts, or updates the row with the same id. Re-inserting a deleted row restores it (undo). */
    @Upsert
    void upsert(ExpenseEntity expense);

    @Delete
    int delete(ExpenseEntity expense);

    @Query("SELECT COUNT(*) FROM expenses")
    int count();
}
