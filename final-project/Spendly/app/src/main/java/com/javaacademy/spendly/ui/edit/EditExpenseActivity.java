package com.javaacademy.spendly.ui.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.javaacademy.spendly.R;
import com.javaacademy.spendly.SpendlyApp;
import com.javaacademy.spendly.databinding.ActivityEditExpenseBinding;
import com.javaacademy.spendly.domain.Category;
import com.javaacademy.spendly.domain.Expense;
import com.javaacademy.spendly.domain.ExpenseValidator.Field;
import com.javaacademy.spendly.domain.ExpenseValidator.Problem;
import com.javaacademy.spendly.domain.Money;
import com.javaacademy.spendly.ui.SystemBars;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.EnumMap;
import java.util.Map;

/** Create a new expense or edit an existing one. */
public class EditExpenseActivity extends AppCompatActivity {

    private static final String EXTRA_ID = "expense_id";
    private static final String STATE_PREFILLED = "prefilled";
    private static final String DATE_PICKER_TAG = "date_picker";
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);

    private ActivityEditExpenseBinding binding;
    private EditExpenseViewModel viewModel;
    private final Map<Category, Chip> chips = new EnumMap<>(Category.class);
    private boolean prefilled;

    /** The only way to start this screen: keeps the extra's key private (lesson 18). */
    public static Intent intent(Context context, long expenseId) {
        return new Intent(context, EditExpenseActivity.class).putExtra(EXTRA_ID, expenseId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        binding = ActivityEditExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SystemBars.padForSystemBars(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        prefilled = savedInstanceState != null && savedInstanceState.getBoolean(STATE_PREFILLED);
        viewModel = new ViewModelProvider(this, SpendlyApp.from(this).container().viewModelFactory())
                .get(EditExpenseViewModel.class);
        viewModel.start(getIntent().getLongExtra(EXTRA_ID, Expense.NEW_ID));
        setTitle(viewModel.isEditing() ? R.string.edit_expense : R.string.new_expense);

        buildCategoryChips();
        binding.dateInput.setOnClickListener(v -> showDatePicker());
        binding.saveButton.setOnClickListener(v -> save());
        reattachDatePicker();

        viewModel.existing().observe(this, expense -> {
            if (expense == null || prefilled) return;      // EditTexts restore their own text after rotation
            prefilled = true;
            binding.titleInput.setText(expense.title());
            binding.amountInput.setText(Money.toPlainString(expense.amountCents()));
            binding.noteInput.setText(expense.note());
        });
        viewModel.category().observe(this, c -> {
            Chip chip = chips.get(c);
            if (chip != null && !chip.isChecked()) chip.setChecked(true);
        });
        viewModel.date().observe(this, d -> binding.dateInput.setText(d.format(DATE)));
        viewModel.problems().observe(this, this::showProblems);
        viewModel.finished().observe(this, done -> {
            if (done) finish();
        });
    }

    private void buildCategoryChips() {
        LayoutInflater inflater = getLayoutInflater();
        for (Category c : Category.values()) {
            Chip chip = (Chip) inflater.inflate(R.layout.item_choice_chip, binding.categoryChips, false);
            chip.setId(View.generateViewId());
            chip.setText(c.emoji() + " " + c.displayName());
            chip.setTag(c);
            chips.put(c, chip);
            binding.categoryChips.addView(chip);
        }
        binding.categoryChips.setOnCheckedStateChangeListener((group, ids) -> {
            if (!ids.isEmpty() && group.findViewById(ids.get(0)).getTag() instanceof Category c) {
                viewModel.selectCategory(c);
            }
        });
    }

    private void save() {
        viewModel.save(text(binding.titleLayout), text(binding.amountLayout), text(binding.noteLayout));
    }

    private static String text(TextInputLayout layout) {
        return layout.getEditText() == null || layout.getEditText().getText() == null
                ? "" : layout.getEditText().getText().toString();
    }

    private void showProblems(Map<Field, Problem> problems) {
        binding.titleLayout.setError(message(problems.get(Field.TITLE)));
        binding.amountLayout.setError(message(problems.get(Field.AMOUNT)));
        binding.dateLayout.setError(message(problems.get(Field.DATE)));
    }

    private String message(Problem problem) {
        if (problem == null) return null;
        return getString(switch (problem) {
            case REQUIRED -> R.string.error_required;
            case TOO_LONG -> R.string.error_too_long;
            case NOT_A_NUMBER -> R.string.error_not_a_number;
            case NOT_POSITIVE -> R.string.error_not_positive;
            case TOO_LARGE -> R.string.error_too_large;
            case IN_THE_FUTURE -> R.string.error_future_date;
        });
    }

    // ---------------------------------------------------------------- date picker

    private void showDatePicker() {
        LocalDate current = viewModel.date().getValue();
        long selection = (current != null ? current : viewModel.today()).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.date)
                .setSelection(selection)
                .setCalendarConstraints(new CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointBackward.now())     // no future dates
                        .build())
                .build();
        attach(picker);
        picker.show(getSupportFragmentManager(), DATE_PICKER_TAG);
    }

    /** After rotation the picker is restored by the FragmentManager, but our listener isn't. */
    @SuppressWarnings("unchecked")
    private void reattachDatePicker() {
        Fragment f = getSupportFragmentManager().findFragmentByTag(DATE_PICKER_TAG);
        if (f instanceof MaterialDatePicker<?> picker) attach((MaterialDatePicker<Long>) picker);
    }

    private void attach(MaterialDatePicker<Long> picker) {
        picker.addOnPositiveButtonClickListener(millis ->
                viewModel.selectDate(Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()));
    }

    // ---------------------------------------------------------------- menu & state

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        menu.findItem(R.id.action_delete).setVisible(viewModel.isEditing());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.delete_title)
                    .setMessage(R.string.delete_message)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(R.string.delete, (dialog, which) -> viewModel.delete())
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_PREFILLED, prefilled);
    }
}
