package com.javaacademy.spendly.data;

import com.javaacademy.spendly.data.local.ExpenseEntity;
import com.javaacademy.spendly.domain.Category;
import com.javaacademy.spendly.domain.Expense;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Converts between database rows and domain records. Pure functions — unit-tested on the JVM. */
public final class ExpenseMapper {

    private ExpenseMapper() { }

    public static Expense toDomain(ExpenseEntity e) {
        return new Expense(e.id, e.title, e.amountCents, Category.fromName(e.category), LocalDate.ofEpochDay(e.date), e.note);
    }

    public static ExpenseEntity toEntity(Expense e) {
        return new ExpenseEntity(e.id(), e.title(), e.amountCents(), e.category().name(), e.date().toEpochDay(), e.note());
    }

    public static List<Expense> toDomainList(List<ExpenseEntity> entities) {
        List<Expense> out = new ArrayList<>(entities.size());
        for (ExpenseEntity e : entities) out.add(toDomain(e));
        return out;
    }
}
