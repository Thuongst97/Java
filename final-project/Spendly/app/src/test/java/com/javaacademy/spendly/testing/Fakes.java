package com.javaacademy.spendly.testing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.javaacademy.spendly.data.ExpenseRepository;
import com.javaacademy.spendly.data.SettingsStore;
import com.javaacademy.spendly.data.remote.ExchangeRateService;
import com.javaacademy.spendly.domain.Expense;
import com.javaacademy.spendly.domain.Settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** In-memory fakes for unit tests (lesson 16: prefer fakes to mocks for data). */
public final class Fakes {

    private Fakes() { }

    public static final class FakeExpenseRepository implements ExpenseRepository {
        private final List<Expense> items = new ArrayList<>();
        private final MutableLiveData<List<Expense>> live = new MutableLiveData<>(List.of());
        private long nextId = 1;

        public FakeExpenseRepository(Expense... initial) {
            for (Expense e : initial) {
                items.add(e);
                nextId = Math.max(nextId, e.id() + 1);
            }
            publish();
        }

        @Override public LiveData<List<Expense>> observeAll() { return live; }

        @Override public LiveData<Expense> observe(long id) {
            return new MutableLiveData<>(items.stream().filter(e -> e.id() == id).findFirst().orElse(null));
        }

        @Override public void save(Expense expense) {
            Expense toStore = expense.isNew() ? expense.withId(nextId++) : expense;
            items.removeIf(e -> e.id() == toStore.id());
            items.add(toStore);
            publish();
        }

        @Override public void delete(Expense expense) {
            items.removeIf(e -> e.id() == expense.id());
            publish();
        }

        public List<Expense> items() { return List.copyOf(items); }

        private void publish() {
            List<Expense> sorted = new ArrayList<>(items);
            sorted.sort(Comparator.comparing(Expense::date).reversed());
            live.setValue(sorted);
        }
    }

    public static final class FakeSettingsStore implements SettingsStore {
        private final MutableLiveData<Settings> live;

        public FakeSettingsStore(Settings initial) { live = new MutableLiveData<>(initial); }

        @Override public LiveData<Settings> observe() { return live; }
        @Override public Settings current() { return live.getValue(); }
        @Override public void update(Settings settings) { live.setValue(settings); }
    }

    public static final class FakeRateService implements ExchangeRateService {
        public double rate = 2.0;
        public boolean fail;
        public int calls;

        @Override public double rate(String from, String to) throws IOException {
            calls++;
            if (fail) throw new IOException("offline");
            return rate;
        }
    }
}
