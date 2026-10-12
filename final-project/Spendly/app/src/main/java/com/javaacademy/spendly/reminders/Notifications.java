package com.javaacademy.spendly.reminders;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.javaacademy.spendly.R;
import com.javaacademy.spendly.ui.list.MainActivity;

/** Notification channel setup and the daily reminder notification. */
public final class Notifications {

    public static final String CHANNEL_REMINDERS = "reminders";
    private static final int ID_DAILY_REMINDER = 1;

    private Notifications() { }

    /** Channels are required since Android 8 (our minSdk); creating an existing one is a no-op. */
    public static void createChannel(Context context) {
        NotificationChannel channel = new NotificationChannel(CHANNEL_REMINDERS,
                context.getString(R.string.channel_reminders), NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(context.getString(R.string.channel_reminders_description));
        context.getSystemService(NotificationManager.class).createNotificationChannel(channel);
    }

    public static void showDailyReminder(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return;                                   // the user said no — respect it
        }
        Intent open = new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent tap = PendingIntent.getActivity(context, 0, open, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_REMINDERS)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.reminder_title))
                .setContentText(context.getString(R.string.reminder_text))
                .setContentIntent(tap)
                .setAutoCancel(true);
        NotificationManagerCompat.from(context).notify(ID_DAILY_REMINDER, builder.build());
    }
}
