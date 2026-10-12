package com.javaacademy.spendly.ui.edit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.javaacademy.spendly.data.ExpenseRepository;
import com.javaacademy.spendly.domain.Category;
import com.javaacademy.spendly.domain.Expense;
import com.javaacademy.spendly.domain.ExpenseValidator;
import com.javaacademy.spendly.domain.ExpenseValidator.Field;
import com.javaacademy.spendly.domain.ExpenseValidator.Problem;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

/** State holder for the create/edit screen. Validation lives in the domain layer. */
public final class EditExpenseViewModel extends ViewModel {

    private final ExpenseRepository repository;
    private final Clock clock;

    private long id = Expense.NEW_ID;
    private boolean started;

    private final MediatorLiveData<Expense> existing = new MediatorLiveData<>();
    private final MutableLiveData<Category> category = new MutableLiveData<>(Category.FOOD);
    private final MutableLiveData<LocalDate> date;
    private final MutableLiveData<Map<Field, Problem>> problems = new MutableLiveData<>(Collections.emptyMap());
    private final MutableLiveData<Boolean> finished = new MutableLiveData<>(false);

    public EditExpenseViewModel(ExpenseRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
        this.date = new MutableLiveData<>(LocalDate.now(clock));
    }

    /** Called by the Activity; only the first call counts, so rotation doesn't reload. */
    public void start(long expenseId) {
        if (started) return;
        started = true;
        id = expenseId;
        if (expenseId == Expense.NEW_ID) return;
        LiveData<Expense> source = repository.observe(expenseId);
        existing.addSource(source, expense -> {
            existing.removeSource(source);     // load once; later edits come from the form
            existing.setValue(expense);
            if (expense != null) {
                category.setValue(expense.category());
                date.setValue(expense.date());
            }
        });
    }

    public boolean isEditing() { return id != Expense.NEW_ID; }

    public LiveData<Expense> existing() { return existing; }
    public LiveData<Category> category() { return category; }
    public LiveData<LocalDate> date() { return date; }
    public LiveData<Map<Field, Problem>> problems() { return problems; }
    public LiveData<Boolean> finished() { return finished; }

    public LocalDate today() { return LocalDate.now(clock); }

    public void selectCategory(Category c) { category.setValue(c); }

    public void selectDate(LocalDate d) { date.setValue(d); }

    public void save(String title, String amount, String note) {
        ExpenseValidator.Result result = ExpenseValidator.validate(
                id, title, amount, category.getValue(), date.getValue(), note, today());
        problems.setValue(result.problems());
        if (result.isValid()) {
            repository.save(result.expense());
            finished.setValue(true);
        }
    }

    public void delete() {
        Expense e = existing.getValue();
        if (e != null) {
            repository.delete(e);
            finished.setValue(true);
        }
    }
}
