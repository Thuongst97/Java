package com.javaacademy.spendly.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.javaacademy.spendly.domain.Settings;
import com.javaacademy.spendly.domain.ThemeMode;

/**
 * {@link SettingsStore} backed by {@link SharedPreferences}: a tiny key-value file, perfect for a
 * handful of preferences (lesson 20). Reads are fast enough for the main thread; writes use
 * {@code apply()}, which commits to disk asynchronously.
 */
public final class SharedPrefsSettingsStore implements SettingsStore {

    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_CONVERT_TO = "convert_to";
    private static final String KEY_THEME = "theme";
    private static final String KEY_REMINDER = "daily_reminder";

    private final SharedPreferences prefs;
    private final MutableLiveData<Settings> live;

    public SharedPrefsSettingsStore(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        live = new MutableLiveData<>(read());
    }

    @Override
    public LiveData<Settings> observe() {
        return live;
    }

    @Override
    public Settings current() {
        Settings value = live.getValue();
        return value != null ? value : read();
    }

    @Override
    public void update(Settings s) {
        prefs.edit()
                .putString(KEY_CURRENCY, s.currency())
                .putString(KEY_CONVERT_TO, s.convertTo())
                .putString(KEY_THEME, s.theme().name())
                .putBoolean(KEY_REMINDER, s.dailyReminder())
                .apply();
        live.setValue(s);      // settings are changed from the UI, i.e. on the main thread
    }

    private Settings read() {
        Settings d = Settings.DEFAULT;
        return new Settings(
                prefs.getString(KEY_CURRENCY, d.currency()),
                prefs.getString(KEY_CONVERT_TO, d.convertTo()),
                ThemeMode.fromName(prefs.getString(KEY_THEME, d.theme().name())),
                prefs.getBoolean(KEY_REMINDER, d.dailyReminder()));
    }
}
