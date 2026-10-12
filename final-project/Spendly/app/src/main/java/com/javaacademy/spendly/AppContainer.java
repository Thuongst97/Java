package com.javaacademy.spendly;

import android.content.Context;

import com.javaacademy.spendly.data.ExpenseRepository;
import com.javaacademy.spendly.data.RoomExpenseRepository;
import com.javaacademy.spendly.data.SettingsStore;
import com.javaacademy.spendly.data.SharedPrefsSettingsStore;
import com.javaacademy.spendly.data.local.SpendlyDatabase;
import com.javaacademy.spendly.data.remote.ExchangeRateService;
import com.javaacademy.spendly.data.remote.FrankfurterRateService;
import com.javaacademy.spendly.ui.ViewModelFactory;

import java.time.Clock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Manual dependency injection (lesson 19): the one place that knows how to build every
 * long-lived object. Created once in {@link SpendlyApp#onCreate()}, so each dependency is a
 * single shared instance for the whole process. Hilt/Dagger generate this kind of class for you.
 */
public final class AppContainer {

    private final ExecutorService io = Executors.newFixedThreadPool(2);
    private final Clock clock = Clock.systemDefaultZone();
    private final SpendlyDatabase database;
    private final ExpenseRepository expenseRepository;
    private final SettingsStore settingsStore;
    private final ExchangeRateService rateService = new FrankfurterRateService();
    private final ViewModelFactory viewModelFactory;

    public AppContainer(Context context) {
        database = SpendlyDatabase.create(context);
        expenseRepository = new RoomExpenseRepository(database.expenseDao(), io);
        settingsStore = new SharedPrefsSettingsStore(context);
        viewModelFactory = new ViewModelFactory(this);
    }

    public SpendlyDatabase database() { return database; }
    public ExpenseRepository expenseRepository() { return expenseRepository; }
    public SettingsStore settingsStore() { return settingsStore; }
    public ExchangeRateService rateService() { return rateService; }
    public ExecutorService io() { return io; }
    public Clock clock() { return clock; }
    public ViewModelFactory viewModelFactory() { return viewModelFactory; }
}
