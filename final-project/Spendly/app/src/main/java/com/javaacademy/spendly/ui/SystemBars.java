package com.javaacademy.spendly.ui;

import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Apps targeting Android 15+ are drawn edge-to-edge, behind the status and navigation bars.
 * This pads a screen's root view so content stays clear of the bars and the keyboard.
 */
public final class SystemBars {

    private SystemBars() { }

    public static void padForSystemBars(View root) {
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }
}
