package com.javaacademy.spendly.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.javaacademy.spendly.data.local.ExpenseDao;
import com.javaacademy.spendly.data.local.ExpenseEntity;
import com.javaacademy.spendly.data.local.SpendlyDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/** Runs against a real SQLite database on a device: ./gradlew connectedDebugAndroidTest */
@RunWith(AndroidJUnit4.class)
public class ExpenseDaoTest {

    @Rule
    public InstantTaskExecutorRule instantLiveData = new InstantTaskExecutorRule();

    private SpendlyDatabase db;
    private ExpenseDao dao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, SpendlyDatabase.class)
                .allowMainThreadQueries()               // fine in tests, never in the app
                .build();
        dao = db.expenseDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    private static long day(String iso) {
        return LocalDate.parse(iso).toEpochDay();
    }

    /** Waits for a LiveData to emit, then returns the value. */
    private static <T> T await(LiveData<T> live) throws InterruptedException {
        AtomicReference<T> value = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        live.observeForever(new androidx.lifecycle.Observer<T>() {
            @Override
            public void onChanged(T t) {
                value.set(t);
                latch.countDown();
                live.removeObserver(this);
            }
        });
        assertTrue("LiveData never emitted", latch.await(2, TimeUnit.SECONDS));
        return value.get();
    }

    @Test
    public void upsertInsertsThenReplaces() throws InterruptedException {
        dao.upsert(new ExpenseEntity(0, "Coffee", 350, "FOOD", day("2026-03-10"), ""));
        List<ExpenseEntity> all = await(dao.observeAll());
        assertEquals(1, all.size());
        long id = all.get(0).id;

        dao.upsert(new ExpenseEntity(id, "Coffee", 400, "FOOD", day("2026-03-10"), "large"));
        ExpenseEntity updated = await(dao.observeById(id));
        assertEquals(400, updated.amountCents);
        assertEquals("large", updated.note);
        assertEquals(1, dao.count());
    }

    @Test
    public void observeAllIsNewestFirstAndBetweenIsInclusive() throws InterruptedException {
        dao.upsert(new ExpenseEntity(0, "Old", 100, "OTHER", day("2026-01-05"), ""));
        dao.upsert(new ExpenseEntity(0, "New", 200, "OTHER", day("2026-03-01"), ""));
        dao.upsert(new ExpenseEntity(0, "Mid", 300, "OTHER", day("2026-02-15"), ""));

        List<ExpenseEntity> all = await(dao.observeAll());
        assertEquals(Arrays.asList("New", "Mid", "Old"), all.stream().map(e -> e.title).collect(Collectors.toList()));
        assertEquals(1, dao.getBetween(day("2026-02-15"), day("2026-02-15")).size());
    }

    @Test
    public void deleteThenUpsertWithSameIdRestoresTheRow() throws InterruptedException {
        dao.upsert(new ExpenseEntity(0, "Taxi", 1_850, "TRANSPORT", day("2026-03-12"), ""));
        ExpenseEntity taxi = await(dao.observeAll()).get(0);
        dao.delete(taxi);
        assertEquals(0, dao.count());
        dao.upsert(taxi);                                // what "Undo" does
        assertEquals(taxi.id, await(dao.observeAll()).get(0).id);
    }
}
