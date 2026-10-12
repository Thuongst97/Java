// Top-level build file: declare plugins once (apply false) so every module uses the same versions.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.room) apply false
}
