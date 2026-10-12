package com.javaacademy.spendly.data.local;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * The database row for an expense. Room needs a plain class with a constructor matching
 * its fields, so this is separate from the domain {@code Expense} record; {@code ExpenseMapper}
 * converts between the two (lesson 20).
 */
@Entity(tableName = "expenses", indices = {@Index("date")})
public class ExpenseEntity {

    @PrimaryKey(autoGenerate = true)
    public final long id;

    @NonNull
    public final String title;

    @ColumnInfo(name = "amount_cents")
    public final long amountCents;

    /** {@code Category.name()} — never the ordinal. */
    @NonNull
    public final String category;

    /** {@code LocalDate.toEpochDay()} — sorts and compares correctly as a number. */
    public final long date;

    @NonNull
    public final String note;

    public ExpenseEntity(long id, @NonNull String title, long amountCents, @NonNull String category,
                         long date, @NonNull String note) {
        this.id = id;
        this.title = title;
        this.amountCents = amountCents;
        this.category = category;
        this.date = date;
        this.note = note;
    }
}
