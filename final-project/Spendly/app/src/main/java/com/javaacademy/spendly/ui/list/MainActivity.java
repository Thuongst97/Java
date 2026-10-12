package com.javaacademy.spendly.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.javaacademy.spendly.R;
import com.javaacademy.spendly.SpendlyApp;
import com.javaacademy.spendly.databinding.ActivityMainBinding;
import com.javaacademy.spendly.domain.Category;
import com.javaacademy.spendly.domain.Expense;
import com.javaacademy.spendly.domain.ExpenseListState;
import com.javaacademy.spendly.domain.Money;
import com.javaacademy.spendly.domain.MonthlySummary;
import com.javaacademy.spendly.domain.Settings;
import com.javaacademy.spendly.ui.SystemBars;
import com.javaacademy.spendly.ui.edit.EditExpenseActivity;
import com.javaacademy.spendly.ui.settings.SettingsActivity;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** The list screen: month navigation, summary, category filter, search and the expense list. */
public class MainActivity extends AppCompatActivity {

    private static final DateTimeFormatter MONTH = DateTimeFormatter.ofPattern("MMMM yyyy");

    private ActivityMainBinding binding;
    private ExpenseListViewModel viewModel;
    private ExpenseAdapter adapter;
    private Settings settings = Settings.DEFAULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SystemBars.padForSystemBars(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        viewModel = new ViewModelProvider(this, SpendlyApp.from(this).container().viewModelFactory())
                .get(ExpenseListViewModel.class);

        adapter = new ExpenseAdapter(this::openEditor);
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(adapter);
        new ItemTouchHelper(swipeToDelete).attachToRecyclerView(binding.list);

        binding.previousMonth.setOnClickListener(v -> viewModel.previousMonth());
        binding.nextMonth.setOnClickListener(v -> viewModel.nextMonth());
        binding.convertButton.setOnClickListener(v -> viewModel.convertTotal());
        binding.fab.setOnClickListener(v -> openEditor(null));
        buildCategoryChips();

        viewModel.settings().observe(this, s -> {
            settings = s;
            adapter.setCurrency(s.currency());
            binding.convertButton.setText(getString(R.string.convert_to, s.convertTo()));
            binding.convertButton.setVisibility(s.currency().equals(s.convertTo()) ? View.GONE : View.VISIBLE);
            ExpenseListState current = viewModel.state().getValue();
            if (current != null) render(current);
        });
        viewModel.state().observe(this, this::render);
        viewModel.conversion().observe(this, this::renderConversion);
    }

    private void buildCategoryChips() {
        LayoutInflater inflater = getLayoutInflater();
        Chip all = (Chip) inflater.inflate(R.layout.item_filter_chip, binding.categoryChips, false);
        all.setId(View.generateViewId());
        all.setText(R.string.all_categories);
        binding.categoryChips.addView(all);
        for (Category c : Category.values()) {
            Chip chip = (Chip) inflater.inflate(R.layout.item_filter_chip, binding.categoryChips, false);
            chip.setId(View.generateViewId());
            chip.setText(c.emoji() + " " + c.displayName());
            chip.setTag(c);
            binding.categoryChips.addView(chip);
        }
        binding.categoryChips.check(all.getId());
        binding.categoryChips.setOnCheckedStateChangeListener((group, checkedIds) -> {
            Object tag = checkedIds.isEmpty() ? null : group.findViewById(checkedIds.get(0)).getTag();
            viewModel.setCategory(tag instanceof Category category ? category : null);
        });
    }

    private void render(ExpenseListState state) {
        Locale locale = Locale.getDefault();
        MonthlySummary summary = state.summary();
        binding.monthLabel.setText(state.month().format(MONTH.withLocale(locale)));
        binding.nextMonth.setEnabled(viewModel.canGoForward());
        binding.total.setText(Money.format(summary.totalCents(), settings.currency(), locale));

        if (summary.count() == 0) {
            binding.details.setText(R.string.no_expenses_this_month);
        } else {
            String top = summary.topCategory().map(c -> c.emoji() + " " + c.displayName()).orElse("");
            String perDay = Money.format(summary.averagePerDayCents(viewModel.today()), settings.currency(), locale);
            binding.details.setText(getResources().getQuantityString(R.plurals.summary_details, summary.count(),
                    summary.count(), top, perDay));
        }
        renderCategoryBar(summary);

        adapter.submitList(state.items());
        binding.empty.setVisibility(state.isEmpty() ? View.VISIBLE : View.GONE);
        binding.empty.setText(state.filter().isActive() ? R.string.empty_filtered : R.string.empty_month);
    }

    /** A stacked bar: one coloured segment per category, as wide as its share of the month. */
    private void renderCategoryBar(MonthlySummary summary) {
        LinearLayout bar = binding.categoryBar;
        bar.removeAllViews();
        bar.setVisibility(summary.totalCents() == 0 ? View.GONE : View.VISIBLE);
        for (Category c : Category.values()) {
            float share = (float) summary.share(c);
            if (share <= 0) continue;
            View segment = new View(this);
            segment.setBackgroundColor(c.color());
            segment.setContentDescription(c.displayName());
            bar.addView(segment, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, share));
        }
    }

    private void renderConversion(Conversion c) {
        switch (c.status()) {
            case IDLE -> binding.converted.setVisibility(View.GONE);
            case LOADING -> show(getString(R.string.converting));
            case DONE -> show(getString(R.string.converted_total, Money.format(c.cents(), c.currency(), Locale.getDefault())));
            case FAILED -> show(getString(R.string.conversion_failed));
        }
        binding.convertButton.setEnabled(c.status() != Conversion.Status.LOADING);
    }

    private void show(String text) {
        binding.converted.setText(text);
        binding.converted.setVisibility(View.VISIBLE);
    }

    private void openEditor(Expense expense) {
        startActivity(EditExpenseActivity.intent(this, expense == null ? Expense.NEW_ID : expense.id()));
    }

    private final ItemTouchHelper.SimpleCallback swipeToDelete =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder from,
                                      @NonNull RecyclerView.ViewHolder to) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder holder, int direction) {
                    int position = holder.getBindingAdapterPosition();
                    if (position == RecyclerView.NO_POSITION) return;
                    Expense expense = adapter.itemAt(position);
                    viewModel.delete(expense);
                    Snackbar.make(binding.coordinator, getString(R.string.deleted, expense.title()), Snackbar.LENGTH_LONG)
                            .setAction(R.string.undo, v -> viewModel.undoDelete())
                            .setAnchorView(binding.fab)
                            .show();
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (search != null) {
            search.setQueryHint(getString(R.string.search_hint));
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    search.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    viewModel.setQuery(newText);
                    return true;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
