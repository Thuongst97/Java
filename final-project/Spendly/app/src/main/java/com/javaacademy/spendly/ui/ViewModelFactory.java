package com.javaacademy.spendly.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.javaacademy.spendly.AppContainer;
import com.javaacademy.spendly.ui.edit.EditExpenseViewModel;
import com.javaacademy.spendly.ui.list.ExpenseListViewModel;

/**
 * Tells {@link ViewModelProvider} how to construct our ViewModels, whose constructors take
 * dependencies. The provider calls this only when no ViewModel exists yet for the screen, so
 * the same instance survives rotation (lesson 19).
 */
public final class ViewModelFactory implements ViewModelProvider.Factory {

    private final AppContainer container;

    public ViewModelFactory(AppContainer container) {
        this.container = container;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == ExpenseListViewModel.class) {
            return modelClass.cast(new ExpenseListViewModel(container.expenseRepository(), container.settingsStore(),
                    container.rateService(), container.io(), container.clock()));
        }
        if (modelClass == EditExpenseViewModel.class) {
            return modelClass.cast(new EditExpenseViewModel(container.expenseRepository(), container.clock()));
        }
        throw new IllegalArgumentException("Unknown ViewModel " + modelClass.getName());
    }
}
