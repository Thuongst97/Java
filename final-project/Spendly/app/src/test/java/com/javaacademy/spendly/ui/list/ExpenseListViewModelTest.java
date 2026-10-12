package com.javaacademy.spendly.ui.list;

import static com.javaacademy.spendly.testing.TestData.expense;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.javaacademy.spendly.domain.Category;
import com.javaacademy.spendly.domain.Expense;
import com.javaacademy.spendly.domain.ExpenseListState;
import com.javaacademy.spendly.domain.Settings;
import com.javaacademy.spendly.testing.Fakes.FakeExpenseRepository;
import com.javaacademy.spendly.testing.Fakes.FakeRateService;
import com.javaacademy.spendly.testing.Fakes.FakeSettingsStore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.List;

public class ExpenseListViewModelTest {

    /** Makes LiveData deliver synchronously on the test thread. */
    @Rule
    public InstantTaskExecutorRule instantLiveData = new InstantTaskExecutorRule();

    private final Clock clock = Clock.fixed(Instant.parse("2026-03-14T10:00:00Z"), ZoneOffset.UTC);
    private FakeExpenseRepository repository;
    private FakeRateService rates;
    private ExpenseListViewModel viewModel;
    private ExpenseListState latest;
    private Conversion latestConversion;

    @Before
    public void setUp() {
        repository = new FakeExpenseRepository(
                expense(1, "Coffee", 350, Category.FOOD, "2026-03-10"),
                expense(2, "Taxi", 2_000, Category.TRANSPORT, "2026-03-12"),
                expense(3, "Rent", 90_000, Category.BILLS, "2026-02-01"));
        rates = new FakeRateService();
        viewModel = new ExpenseListViewModel(repository, new FakeSettingsStore(Settings.DEFAULT),
                rates, Runnable::run, clock);                 // Runnable::run = a "direct" executor
        viewModel.state().observeForever(s -> latest = s);    // MediatorLiveData only works while observed
        viewModel.conversion().observeForever(c -> latestConversion = c);
    }

    private List<Long> ids() {
        return latest.items().stream().map(Expense::id).toList();
    }

    @Test
    public void startsOnTheCurrentMonth() {
        assertEquals(YearMonth.of(2026, 3), latest.month());
        assertEquals(List.of(2L, 1L), ids());
        assertEquals(2_350, latest.summary().totalCents());
        assertFalse(viewModel.canGoForward());
    }

    @Test
    public void navigatesMonthsButNotIntoTheFuture() {
        viewModel.previousMonth();
        assertEquals(YearMonth.of(2026, 2), latest.month());
        assertEquals(List.of(3L), ids());
        assertTrue(viewModel.canGoForward());
        viewModel.nextMonth();
        viewModel.nextMonth();                                // ignored: already current
        assertEquals(YearMonth.of(2026, 3), latest.month());
    }

    @Test
    public void filtersByQueryAndCategory() {
        viewModel.setQuery("  taxi ");
        assertEquals(List.of(2L), ids());
        viewModel.setQuery("");
        viewModel.setCategory(Category.FOOD);
        assertEquals(List.of(1L), ids());
        viewModel.setCategory(null);
        assertEquals(2, latest.items().size());
    }

    @Test
    public void deleteAndUndo() {
        Expense taxi = latest.items().get(0);
        viewModel.delete(taxi);
        assertEquals(List.of(1L), ids());
        viewModel.undoDelete();
        assertEquals(List.of(2L, 1L), ids());
        assertEquals(taxi, repository.items().stream().filter(e -> e.id() == 2).findFirst().orElseThrow());
    }

    @Test
    public void convertsTheMonthTotal() {
        rates.rate = 0.5;
        viewModel.convertTotal();
        assertEquals(Conversion.done(1_175, "EUR"), latestConversion);
    }

    @Test
    public void conversionFailureIsReportedAndResetWhenDataChanges() {
        rates.fail = true;
        viewModel.convertTotal();
        assertEquals(Conversion.Status.FAILED, latestConversion.status());
        viewModel.setQuery("coffee");                         // state recomputed → stale conversion cleared
        assertEquals(Conversion.IDLE, latestConversion);
    }
}
