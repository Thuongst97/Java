package com.javaacademy.spendly.ui.settings;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.javaacademy.spendly.R;
import com.javaacademy.spendly.SpendlyApp;
import com.javaacademy.spendly.data.SettingsStore;
import com.javaacademy.spendly.databinding.ActivitySettingsBinding;
import com.javaacademy.spendly.domain.Settings;
import com.javaacademy.spendly.domain.ThemeMode;
import com.javaacademy.spendly.reminders.ReminderScheduler;
import com.javaacademy.spendly.ui.SystemBars;

/**
 * Preferences screen. The settings are a handful of synchronous key-value reads, so this screen
 * talks to the {@link SettingsStore} directly instead of through a ViewModel.
 */
public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private SettingsStore store;

    /** Android 13+ asks the user before an app may post notifications. */
    private final ActivityResultLauncher<String> notificationPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    setReminder(true);
                } else {
                    binding.reminderSwitch.setChecked(false);
                    Snackbar.make(binding.getRoot(), R.string.notifications_denied, Snackbar.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SystemBars.padForSystemBars(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        store = SpendlyApp.from(this).container().settingsStore();
        Settings s = store.current();
        String[] codes = Settings.CURRENCIES.toArray(new String[0]);

        binding.currencyInput.setSimpleItems(codes);
        binding.currencyInput.setText(s.currency(), false);
        binding.currencyInput.setOnItemClickListener((parent, view, position, id) ->
                store.update(store.current().withCurrency(codes[position])));

        binding.convertInput.setSimpleItems(codes);
        binding.convertInput.setText(s.convertTo(), false);
        binding.convertInput.setOnItemClickListener((parent, view, position, id) ->
                store.update(store.current().withConvertTo(codes[position])));

        binding.themeGroup.check(switch (s.theme()) {
            case LIGHT -> R.id.theme_light;
            case DARK -> R.id.theme_dark;
            case SYSTEM -> R.id.theme_system;
        });
        binding.themeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            ThemeMode mode = checkedId == R.id.theme_light ? ThemeMode.LIGHT
                    : checkedId == R.id.theme_dark ? ThemeMode.DARK : ThemeMode.SYSTEM;
            store.update(store.current().withTheme(mode));
            SpendlyApp.applyTheme(mode);          // recreates visible activities in the new theme
        });

        binding.reminderSwitch.setChecked(s.dailyReminder());
        binding.reminderSwitch.setOnCheckedChangeListener(this::onReminderToggled);
    }

    private void onReminderToggled(CompoundButton button, boolean on) {
        if (on && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS);
            return;                                // continues in the permission callback
        }
        setReminder(on);
    }

    private void setReminder(boolean on) {
        store.update(store.current().withDailyReminder(on));
        ReminderScheduler.apply(this, on);
    }
}
