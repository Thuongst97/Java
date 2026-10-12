package com.javaacademy.spendly.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.util.Locale;
import java.util.OptionalLong;

public class MoneyTest {

    @Test
    public void parsesWholeAndDecimalAmounts() {
        assertEquals(OptionalLong.of(1200), Money.parseCents("12"));
        assertEquals(OptionalLong.of(1250), Money.parseCents("12.5"));
        assertEquals(OptionalLong.of(1250), Money.parseCents("12,50"));
        assertEquals(OptionalLong.of(123499), Money.parseCents(" 1 234.99 "));
        assertEquals(OptionalLong.of(-500), Money.parseCents("-5"));
    }

    @Test
    public void rejectsGarbageAndTooManyDecimals() {
        assertFalse(Money.parseCents("abc").isPresent());
        assertFalse(Money.parseCents("").isPresent());
        assertFalse(Money.parseCents(null).isPresent());
        assertFalse(Money.parseCents("1.234").isPresent());
        assertFalse(Money.parseCents("1.2.3").isPresent());
        assertFalse(Money.parseCents("99999999999999999999").isPresent());
    }

    @Test
    public void plainStringIsAlwaysTwoDecimalsWithADot() {
        assertEquals("12.50", Money.toPlainString(1250));
        assertEquals("0.05", Money.toPlainString(5));
    }

    @Test
    public void formatsUsingTheCurrencyAndLocale() {
        assertEquals("$1,234.50", Money.format(123450, "USD", Locale.US));
        assertEquals("¥1,235", Money.format(123460, "JPY", Locale.US));   // yen has no minor unit: 1234.60 → 1235
        assertEquals("¥1,234", Money.format(123450, "JPY", Locale.US));   // half-even: 1234.50 → 1234
    }

    @Test
    public void convertsWithBankersRounding() {
        assertEquals(920, Money.convert(1000, 0.92));
        assertEquals(2, Money.convert(5, 0.5));       // 2.5 rounds to the even neighbour
        assertThrows(IllegalArgumentException.class, () -> Money.convert(100, 0));
    }
}
