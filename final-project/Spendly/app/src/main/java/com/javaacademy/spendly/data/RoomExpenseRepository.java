package com.javaacademy.spendly.data;

import androidx.lifecycle.LiveData;

import com.javaacademy.spendly.data.local.ExpenseDao;
import com.javaacademy.spendly.domain.Expense;

import java.util.List;
import java.util.concurrent.Executor;

/** {@link ExpenseRepository} backed by Room. Writes run on the injected I/O executor. */
public final class RoomExpenseRepository implements ExpenseRepository {

    private final ExpenseDao dao;
    private final Executor io;

    public RoomExpenseRepository(ExpenseDao dao, Executor io) {
        this.dao = dao;
        this.io = io;
    }

    @Override
    public LiveData<List<Expense>> observeAll() {
        return LiveDataTransformations.map(dao.observeAll(), ExpenseMapper::toDomainList);
    }

    @Override
    public LiveData<Expense> observe(long id) {
        return LiveDataTransformations.map(dao.observeById(id), ExpenseMapper::toDomain);
    }

    @Override
    public void save(Expense expense) {
        io.execute(() -> dao.upsert(ExpenseMapper.toEntity(expense)));
    }

    @Override
    public void delete(Expense expense) {
        io.execute(() -> dao.delete(ExpenseMapper.toEntity(expense)));
    }
}
