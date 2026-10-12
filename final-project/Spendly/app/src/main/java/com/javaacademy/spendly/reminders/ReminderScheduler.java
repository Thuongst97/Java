package com.javaacademy.spendly.reminders;

import android.content.Context;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/** Turns the daily reminder on or off with WorkManager (lesson 20). */
public final class ReminderScheduler {

    private static final String WORK_NAME = "daily-expense-reminder";

    private ReminderScheduler() { }

    public static void apply(Context context, boolean enabled) {
        WorkManager workManager = WorkManager.getInstance(context.getApplicationContext());
        if (!enabled) {
            workManager.cancelUniqueWork(WORK_NAME);
            return;
        }
        Duration delay = ReminderTime.delayUntilNext(LocalDateTime.now(), ReminderTime.AT);
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(ReminderWorker.class, 1, TimeUnit.DAYS)
                .setInitialDelay(delay.toMillis(), TimeUnit.MILLISECONDS)
                .build();
        // Unique work: re-enabling replaces the schedule instead of stacking duplicates.
        workManager.enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.UPDATE, request);
    }
}
