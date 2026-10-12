package com.javaacademy.spendly.data;

import androidx.lifecycle.LiveData;

import com.javaacademy.spendly.domain.Settings;

/** Reads and writes user preferences. */
public interface SettingsStore {

    /** The current settings; emits again whenever they change. */
    LiveData<Settings> observe();

    Settings current();

    void update(Settings settings);
}
