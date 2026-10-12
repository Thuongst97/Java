package com.javaacademy.spendly.reminders;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

/** Pure scheduling maths, kept free of Android so it can be unit-tested. */
public final class ReminderTime {

    /** The daily reminder fires at 20:00 local time. */
    public static final LocalTime AT = LocalTime.of(20, 0);

    private ReminderTime() { }

    /** Time from {@code now} until the next occurrence of {@code at} (today if still ahead, else tomorrow). */
    public static Duration delayUntilNext(LocalDateTime now, LocalTime at) {
        LocalDateTime next = now.toLocalDate().atTime(at);
        if (!next.isAfter(now)) next = next.plusDays(1);
        return Duration.between(now, next);
    }
}
