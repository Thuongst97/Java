package com.javaacademy.spendly.reminders;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReminderTimeTest {

    private static final LocalTime EIGHT_PM = LocalTime.of(20, 0);

    @Test
    public void laterTodayIfStillAhead() {
        assertEquals(Duration.ofHours(11).plusMinutes(30),
                ReminderTime.delayUntilNext(LocalDateTime.of(2026, 3, 14, 8, 30), EIGHT_PM));
    }

    @Test
    public void tomorrowIfAlreadyPassedOrExactlyNow() {
        assertEquals(Duration.ofHours(23),
                ReminderTime.delayUntilNext(LocalDateTime.of(2026, 3, 14, 21, 0), EIGHT_PM));
        assertEquals(Duration.ofDays(1),
                ReminderTime.delayUntilNext(LocalDateTime.of(2026, 3, 14, 20, 0), EIGHT_PM));
    }
}
