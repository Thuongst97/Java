package com.javaacademy.spendly.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.javaacademy.spendly.domain.ExpenseValidator.Field;
import com.javaacademy.spendly.domain.ExpenseValidator.Problem;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Map;

public class ExpenseValidatorTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 3, 14);

    private static ExpenseValidator.Result validate(String title, String amount, Category c, LocalDate date) {
        return ExpenseValidator.validate(0, title, amount, c, date, " note ", TODAY);
    }

    @Test
    public void validInputProducesATrimmedExpense() {
        ExpenseValidator.Result r = validate("  Lunch ", "9.95", Category.FOOD, TODAY);
        assertTrue(r.isValid());
        assertEquals("Lunch", r.expense().title());
        assertEquals(995, r.expense().amountCents());
        assertEquals("note", r.expense().note());
        assertTrue(r.problems().isEmpty());
    }

    @Test
    public void reportsEveryProblemAtOnce() {
        ExpenseValidator.Result r = validate(" ", "abc", null, TODAY.plusDays(1));
        assertFalse(r.isValid());
        assertEquals(Map.of(
                Field.TITLE, Problem.REQUIRED,
                Field.AMOUNT, Problem.NOT_A_NUMBER,
                Field.CATEGORY, Problem.REQUIRED,
                Field.DATE, Problem.IN_THE_FUTURE), r.problems());
    }

    @Test
    public void amountRules() {
        assertEquals(Problem.REQUIRED, validate("x", "", Category.FUN, TODAY).problems().get(Field.AMOUNT));
        assertEquals(Problem.NOT_POSITIVE, validate("x", "0", Category.FUN, TODAY).problems().get(Field.AMOUNT));
        assertEquals(Problem.NOT_POSITIVE, validate("x", "-3", Category.FUN, TODAY).problems().get(Field.AMOUNT));
        assertEquals(Problem.TOO_LARGE, validate("x", "10000000.01", Category.FUN, TODAY).problems().get(Field.AMOUNT));
    }

    @Test
    public void titleTooLong() {
        String longTitle = "x".repeat(ExpenseValidator.MAX_TITLE_LENGTH + 1);
        assertEquals(Problem.TOO_LONG, validate(longTitle, "1", Category.FUN, TODAY).problems().get(Field.TITLE));
    }
}
