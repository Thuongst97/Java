package com.javaacademy.spendly.data;

import static org.junit.Assert.assertEquals;

import com.javaacademy.spendly.data.local.ExpenseEntity;
import com.javaacademy.spendly.domain.Category;
import com.javaacademy.spendly.domain.Expense;

import org.junit.Test;

import java.time.LocalDate;

public class ExpenseMapperTest {

    @Test
    public void roundTripsThroughTheEntity() {
        Expense e = new Expense(7, "Taxi", 1_850, Category.TRANSPORT, LocalDate.of(2026, 3, 14), "airport");
        ExpenseEntity entity = ExpenseMapper.toEntity(e);
        assertEquals("TRANSPORT", entity.category);
        assertEquals(LocalDate.of(2026, 3, 14).toEpochDay(), entity.date);
        assertEquals(e, ExpenseMapper.toDomain(entity));
    }

    @Test
    public void unknownCategoryFallsBackToOther() {
        ExpenseEntity entity = new ExpenseEntity(1, "Mystery", 100, "SPACE_TRAVEL", 0, "");
        assertEquals(Category.OTHER, ExpenseMapper.toDomain(entity).category());
    }
}
