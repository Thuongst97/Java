# Spendly — Java Academy final project

A production-style Android expense tracker written in **Java**, built step by step in
[lesson 23](https://thuongst97.github.io/Java/final-project.html).

**Features:** monthly overview with totals, per-category breakdown and daily average · search and
category filters · add/edit with validation and a date picker · swipe to delete with Undo ·
convert the month's total into another currency (live exchange rates) · light/dark/system theme ·
an optional daily reminder notification that works even when the app is closed.

**Stack:** Java 17 language level · Android Views + Material 3 + View Binding · RecyclerView with
ListAdapter · ViewModel + LiveData (unidirectional data flow) · manual dependency injection ·
Room · SharedPreferences · HttpURLConnection + Gson · WorkManager · JUnit 4, Espresso.

```bash
./gradlew assembleDebug              # build
./gradlew installDebug               # install on a connected device/emulator
./gradlew testDebugUnitTest          # JVM unit tests (domain, mappers, ViewModel)
./gradlew connectedDebugAndroidTest  # Room DAO + Espresso tests on a device
./gradlew lintDebug                  # Android Lint (catches APIs newer than minSdk, and more)
```

Requirements: Android Studio (latest stable), JDK 17+ (Android Studio's bundled JBR works), Android SDK 36.

```
app/src/main/java/com/javaacademy/spendly/
├── SpendlyApp.java, AppContainer.java     application + manual DI
├── domain/        pure Java: Expense, Money, filters, summaries, validation (no Android imports)
├── data/          repositories, Room (local/), exchange rates (remote/), settings
├── ui/            list/, edit/, settings/ screens, ViewModels and the ViewModelFactory
└── reminders/     WorkManager worker, scheduling and notifications
```

Room exports its schema to `app/schemas/` on the first build — commit those JSON files so future
migrations can be tested. For release signing, create a git-ignored `keystore.properties`
(see lesson 22).
