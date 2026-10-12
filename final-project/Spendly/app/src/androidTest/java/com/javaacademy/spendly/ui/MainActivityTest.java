package com.javaacademy.spendly.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.javaacademy.spendly.R;
import com.javaacademy.spendly.ui.list.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** A UI smoke test with Espresso (lesson 22). */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activity = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void showsTheSummaryCard() {
        onView(withText(R.string.spent_this_month)).check(matches(isDisplayed()));
        onView(withId(R.id.total)).check(matches(isDisplayed()));
    }

    @Test
    public void fabOpensTheEditor() {
        onView(withId(R.id.fab)).perform(click());
        onView(withText(R.string.new_expense)).check(matches(isDisplayed()));
        onView(withId(R.id.saveButton)).check(matches(isDisplayed()));
    }
}
