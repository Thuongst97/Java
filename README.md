# Java Academy — learn Java from scratch to master

A free, hands-on website that takes you from **installing Android Studio** to **shipping a complete Android app written in Java**. Everything runs in **Android Studio**: plain Java in a Java library module for the language lessons, real Android apps for the rest.

**🌐 Live site:** https://thuongst97.github.io/Java/ (deployed by GitHub Pages from `main`)

| Stage | Lessons |
|---|---|
| 🧰 Getting started | 01 Toolkit: JDK & Android Studio · 02 Hello, Java! |
| 🔤 Fundamentals | 03 Variables, types & operators · 04 Control flow · 05 Methods · 06 Strings & arrays |
| 🏛 Object-oriented Java | 07 Classes & objects · 08 Inheritance, polymorphism & interfaces · 09 Records, enums, sealed types & pattern matching · 10 Exceptions, null & Optional |
| 🧩 Core APIs | 11 Generics · 12 Collections · 13 Lambdas & streams · 14 Files, I/O & date-time |
| ⚡ Advanced | 15 Concurrency · 16 Testing, reflection, JVM & design patterns · 17 **Unit tests: setup & run in Android Studio** |
| 🤖 Android with Java | 18 App fundamentals · 19 Views & layouts · 20 ViewModel, LiveData & repositories · 21 Room, networking & WorkManager · 22 Testing, debugging & publishing |
| 🏁 Final project | 23 **Spendly** — a complete expense-tracker app built in 9 milestones |
| 📋 Reference | A · Cheat sheet & mastery checklist · B · **Java coding rules & best practices** (✗/✓ examples, full refactoring walkthrough) |

Every lesson has examples, exercises with solutions, a quiz, and progress tracking saved in your browser. Runnable examples (▶ **Run**) can be edited and executed in the browser through the free [Compiler Explorer](https://godbolt.org) API; if it's unreachable the page copies the code so you can run it in Android Studio.

## Repository layout

```
java-academy/            The static website (plain HTML/CSS/JS, no build step)
  index.html             Landing page + curriculum
  *.html                 One page per lesson
  assets/css/main.css    Design system (dark + light themes)
  assets/js/core.js      Layout, search, highlighter, live Java runner, quizzes, progress
final-project/Spendly/   The complete Android app (Java) built in lesson 23
tools/
  check_snippets.py      Compiles and runs every runnable example with javac and checks its output
  embed_sources.py       Copies Spendly's real source files into final-project.html
.github/workflows/
  pages.yml              Deploys java-academy/ to GitHub Pages on push to main
  check-snippets.yml     Verifies lesson examples + embedded sources on every push
  android.yml            Builds Spendly, runs unit tests and Lint, uploads the debug APK
```

## Run the site locally

```bash
cd java-academy
python3 -m http.server 8000      # then open http://localhost:8000
```

Opening `index.html` directly from disk also works; only full-text search needs a local server.

## Quality checks

```bash
python3 tools/check_snippets.py           # needs JDK 21+: compiles & runs all runnable examples
python3 tools/embed_sources.py            # re-embed Spendly sources after changing the app
python3 tools/embed_sources.py --check    # what CI runs
```

## Build the final project

Open `final-project/Spendly` in Android Studio, or:

```bash
cd final-project/Spendly
./gradlew assembleDebug testDebugUnitTest lintDebug
```

## Deploying

Pushing to `main` runs `.github/workflows/pages.yml`, which stamps asset URLs for cache busting and publishes `java-academy/` to GitHub Pages. One-time setup: **Settings → Pages → Build and deployment → Source: GitHub Actions**.
