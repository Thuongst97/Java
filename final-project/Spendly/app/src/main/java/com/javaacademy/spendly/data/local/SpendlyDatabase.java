package com.javaacademy.spendly.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/** The Room database. Version 1; future schema changes need a Migration (lesson 20). */
@Database(entities = {ExpenseEntity.class}, version = 1, exportSchema = true)
public abstract class SpendlyDatabase extends RoomDatabase {

    public abstract ExpenseDao expenseDao();

    /** Builds the on-disk database. Call once per process — {@code AppContainer} keeps the instance. */
    public static SpendlyDatabase create(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), SpendlyDatabase.class, "spendly.db").build();
    }
}
