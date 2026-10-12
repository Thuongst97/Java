package com.javaacademy.spendly.reminders;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.javaacademy.spendly.SpendlyApp;
import com.javaacademy.spendly.data.local.ExpenseDao;

import java.time.LocalDate;

/**
 * Runs once a day on a WorkManager background thread — even if the app was closed or the phone
 * rebooted. Only nags if nothing has been logged today.
 */
public class ReminderWorker extends Worker {

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        ExpenseDao dao = SpendlyApp.from(getApplicationContext()).container().database().expenseDao();
        long today = LocalDate.now().toEpochDay();
        if (dao.getBetween(today, today).isEmpty()) {          // blocking query: fine on a worker thread
            Notifications.showDailyReminder(getApplicationContext());
        }
        return Result.success();
    }
}
