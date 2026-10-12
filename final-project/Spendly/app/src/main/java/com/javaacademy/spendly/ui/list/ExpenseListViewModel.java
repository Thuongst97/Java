package com.javaacademy.spendly.ui.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.javaacademy.spendly.data.ExpenseRepository;
import com.javaacademy.spendly.data.SettingsStore;
import com.javaacademy.spendly.data.remote.ExchangeRateService;
import com.javaacademy.spendly.domain.Category;
import com.javaacademy.spendly.domain.Expense;
import com.javaacademy.spendly.domain.ExpenseFilter;
import com.javaacademy.spendly.domain.ExpenseListState;
import com.javaacademy.spendly.domain.Money;
import com.javaacademy.spendly.domain.Settings;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * State holder for the list screen. Inputs (all expenses, selected month, filter) are combined
 * with a {@link MediatorLiveData} into one immutable {@link ExpenseListState} that the Activity
 * simply renders — unidirectional data flow (lesson 19).
 */
public final class ExpenseListViewModel extends ViewModel {

    private final ExpenseRepository repository;
    private final SettingsStore settingsStore;
    private final ExchangeRateService rates;
    private final Executor io;
    private final Clock clock;

    private final LiveData<List<Expense>> allExpenses;
    private final MutableLiveData<YearMonth> month;
    private final MutableLiveData<ExpenseFilter> filter = new MutableLiveData<>(ExpenseFilter.ALL);
    private final MediatorLiveData<ExpenseListState> state = new MediatorLiveData<>();
    private final MutableLiveData<Conversion> conversion = new MutableLiveData<>(Conversion.IDLE);

    private Expense lastDeleted;

    public ExpenseListViewModel(ExpenseRepository repository, SettingsStore settingsStore,
                                ExchangeRateService rates, Executor io, Clock clock) {
        this.repository = repository;
        this.settingsStore = settingsStore;
        this.rates = rates;
        this.io = io;
        this.clock = clock;
        this.allExpenses = repository.observeAll();
        this.month = new MutableLiveData<>(YearMonth.now(clock));

        state.addSource(allExpenses, ignored -> recompute());
        state.addSource(month, ignored -> recompute());
        state.addSource(filter, ignored -> recompute());
    }

    public LiveData<ExpenseListState> state() { return state; }
    public LiveData<Conversion> conversion() { return conversion; }
    public LiveData<Settings> settings() { return settingsStore.observe(); }

    public LocalDate today() { return LocalDate.now(clock); }

    public boolean canGoForward() {
        YearMonth m = month.getValue();
        return m != null && m.isBefore(YearMonth.now(clock));
    }

    public void previousMonth() { month.setValue(requireMonth().minusMonths(1)); }

    public void nextMonth() {
        if (canGoForward()) month.setValue(requireMonth().plusMonths(1));
    }

    public void setQuery(String query) {
        ExpenseFilter current = requireFilter();
        String q = query == null ? "" : query.trim();
        if (!q.equals(current.query())) filter.setValue(current.withQuery(q));
    }

    /** {@code null} shows all categories. */
    public void setCategory(Category category) {
        ExpenseFilter current = requireFilter();
        if (current.category() != category) filter.setValue(current.withCategory(category));
    }

    public void delete(Expense expense) {
        lastDeleted = expense;
        repository.delete(expense);
    }

    public void undoDelete() {
        if (lastDeleted != null) {
            repository.save(lastDeleted);     // same id → the row comes back exactly as it was
            lastDeleted = null;
        }
    }

    /** Converts the month's total into the user's "convert to" currency on a background thread. */
    public void convertTotal() {
        ExpenseListState current = state.getValue();
        if (current == null) return;
        Settings settings = settingsStore.current();
        long total = current.summary().totalCents();
        String from = settings.currency();
        String to = settings.convertTo();
        conversion.setValue(Conversion.loading(to));
        io.execute(() -> {
            try {
                double rate = rates.rate(from, to);
                conversion.postValue(Conversion.done(Money.convert(total, rate), to));   // postValue: we're off the main thread
            } catch (IOException | RuntimeException e) {
                conversion.postValue(Conversion.failed(to));
            }
        });
    }

    private void recompute() {
        List<Expense> all = allExpenses.getValue();
        if (all == null) return;                   // the database hasn't answered yet
        state.setValue(ExpenseListState.build(all, requireMonth(), requireFilter()));
        if (conversion.getValue() != Conversion.IDLE) conversion.setValue(Conversion.IDLE);   // the total changed
    }

    private YearMonth requireMonth() {
        YearMonth m = month.getValue();
        return m != null ? m : YearMonth.now(clock);
    }

    private ExpenseFilter requireFilter() {
        ExpenseFilter f = filter.getValue();
        return f != null ? f : ExpenseFilter.ALL;
    }
}
