package com.javaacademy.spendly;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.color.DynamicColors;
import com.javaacademy.spendly.domain.ThemeMode;
import com.javaacademy.spendly.reminders.Notifications;

/** The Application object lives as long as the process: the right owner for the {@link AppContainer}. */
public class SpendlyApp extends Application {

    private AppContainer container;

    @Override
    public void onCreate() {
        super.onCreate();
        container = new AppContainer(this);
        applyTheme(container.settingsStore().current().theme());
        DynamicColors.applyToActivitiesIfAvailable(this);   // Material You colours on Android 12+
        Notifications.createChannel(this);
    }

    public AppContainer container() {
        return container;
    }

    public static SpendlyApp from(Context context) {
        return (SpendlyApp) context.getApplicationContext();
    }

    public static void applyTheme(ThemeMode mode) {
        AppCompatDelegate.setDefaultNightMode(switch (mode) {
            case LIGHT -> AppCompatDelegate.MODE_NIGHT_NO;
            case DARK -> AppCompatDelegate.MODE_NIGHT_YES;
            case SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        });
    }
}
