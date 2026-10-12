/* =========================================================================
   Java Academy — core runtime
   Shared layout (header, sidebar, pager, footer), full-text search, theme,
   scrollspy, tabs, syntax highlighter, live Java editor (▶ Run), quizzes
   and lesson progress tracking. No build step, no dependencies.
   ========================================================================= */
(function () {
  "use strict";

  /* ------------------------------------------------------------ Site map */
  const SITE = [
    { id: "setup", file: "setup.html", num: "01", group: "Getting Started", level: "Beginner", minutes: 60,
      title: "Toolkit: JDK & Android Studio", short: "Toolkit & Android Studio", icon: "🧰", accent: "#3ddc84",
      desc: "Install Android Studio and its bundled JDK, run plain Java in a library module, set up the SDK, an emulator and a real device." },
    { id: "hello", file: "hello.html", num: "02", group: "Getting Started", level: "Beginner", minutes: 35,
      title: "Hello, Java!", short: "Hello, Java!", icon: "👋", accent: "#f89820",
      desc: "Your first program, how javac and the JVM (and ART on Android) run it, reading input and fixing compiler errors." },
    { id: "basics", file: "basics.html", num: "03", group: "Java Fundamentals", level: "Beginner", minutes: 50,
      title: "Variables, Types & Operators", short: "Variables & Types", icon: "🔤", accent: "#6cb8ff",
      desc: "Primitive and reference types, var, literals, casting, integer overflow, operators and the Math class." },
    { id: "control-flow", file: "control-flow.html", num: "04", group: "Java Fundamentals", level: "Beginner", minutes: 45,
      title: "Control Flow", short: "Control Flow", icon: "🔀", accent: "#6cb8ff",
      desc: "if/else, the ternary operator, classic and arrow switch, switch expressions, loops, break, continue and labels." },
    { id: "methods", file: "methods.html", num: "05", group: "Java Fundamentals", level: "Beginner", minutes: 45,
      title: "Methods", short: "Methods", icon: "🧮", accent: "#6cb8ff",
      desc: "Parameters and return values, overloading, pass-by-value, varargs, scope, static methods and recursion." },
    { id: "strings-arrays", file: "strings-arrays.html", num: "06", group: "Java Fundamentals", level: "Beginner", minutes: 55,
      title: "Strings & Arrays", short: "Strings & Arrays", icon: "🧵", accent: "#6cb8ff",
      desc: "The String API, immutability, StringBuilder, text blocks, formatting, arrays, 2-D arrays and java.util.Arrays." },
    { id: "classes", file: "classes.html", num: "07", group: "Object-Oriented Java", level: "Intermediate", minutes: 60,
      title: "Classes & Objects", short: "Classes & Objects", icon: "🏛️", accent: "#e05fd4",
      desc: "Fields, constructors, this, encapsulation, access modifiers, static members, equals/hashCode/toString and immutability." },
    { id: "inheritance", file: "inheritance.html", num: "08", group: "Object-Oriented Java", level: "Intermediate", minutes: 60,
      title: "Inheritance, Polymorphism & Interfaces", short: "Inheritance & Interfaces", icon: "🧬", accent: "#e05fd4",
      desc: "extends and super, overriding, dynamic dispatch, abstract classes, interfaces with default methods and composition." },
    { id: "modern-types", file: "modern-types.html", num: "09", group: "Object-Oriented Java", level: "Intermediate", minutes: 55,
      title: "Records, Enums, Sealed Types & Pattern Matching", short: "Records, Enums & Patterns", icon: "💎", accent: "#e05fd4",
      desc: "Records, rich enums, sealed hierarchies, pattern matching for instanceof and switch, nested and anonymous classes." },
    { id: "exceptions", file: "exceptions.html", num: "10", group: "Object-Oriented Java", level: "Intermediate", minutes: 50,
      title: "Exceptions, Null & Optional", short: "Exceptions & Optional", icon: "🛡️", accent: "#e05fd4",
      desc: "Checked vs unchecked exceptions, try/catch/finally, try-with-resources, custom exceptions, null safety and Optional." },
    { id: "generics", file: "generics.html", num: "11", group: "Core APIs & Functional Java", level: "Intermediate", minutes: 45,
      title: "Generics", short: "Generics", icon: "🧩", accent: "#ff8a4c",
      desc: "Generic classes and methods, bounded types, wildcards and PECS, type erasure and its consequences." },
    { id: "collections", file: "collections.html", num: "12", group: "Core APIs & Functional Java", level: "Intermediate", minutes: 65,
      title: "The Collections Framework", short: "Collections", icon: "📚", accent: "#ff8a4c",
      desc: "List, Set, Map, Queue and Deque, choosing an implementation, Comparable and Comparator, immutable collections." },
    { id: "functional", file: "functional.html", num: "13", group: "Core APIs & Functional Java", level: "Advanced", minutes: 65,
      title: "Lambdas, Method References & Streams", short: "Lambdas & Streams", icon: "λ", accent: "#ff8a4c",
      desc: "Functional interfaces, lambdas, method references, the Stream API, collectors, grouping and Optional pipelines." },
    { id: "io-time", file: "io-time.html", num: "14", group: "Core APIs & Functional Java", level: "Intermediate", minutes: 45,
      title: "Files, I/O & Date-Time", short: "Files, I/O & Time", icon: "🗂️", accent: "#ff8a4c",
      desc: "java.nio.file, readers and writers, character encodings, CSV parsing and the java.time API." },
    { id: "concurrency", file: "concurrency.html", num: "15", group: "Advanced Java", level: "Advanced", minutes: 75,
      title: "Concurrency", short: "Concurrency", icon: "⚡", accent: "#ff6b7a",
      desc: "Threads, ExecutorService, race conditions, synchronized, atomics, concurrent collections, CompletableFuture and virtual threads." },
    { id: "advanced", file: "advanced.html", num: "16", group: "Advanced Java", level: "Master", minutes: 70,
      title: "Testing, Reflection, JVM & Design Patterns", short: "Testing, JVM & Patterns", icon: "🎓", accent: "#ff6b7a",
      desc: "JUnit 5, test doubles, annotations and reflection, memory and garbage collection, and the design patterns you'll meet on Android." },
    { id: "unit-tests", file: "unit-tests.html", num: "17", group: "Advanced Java", level: "Advanced", minutes: 50,
      title: "Unit Tests: Setup & Run in Android Studio", short: "Unit Tests: Setup & Run", icon: "🧪", accent: "#ff6b7a",
      desc: "Set up JUnit 5 and JUnit 4, write and run tests from the gutter, Gradle and CI, read reports, measure coverage, mock with Mockito." },
    { id: "android-basics", file: "android-basics.html", num: "18", group: "Android with Java", level: "Intermediate", minutes: 60,
      title: "Android App Fundamentals", short: "Android Fundamentals", icon: "🤖", accent: "#3ddc84",
      desc: "Project anatomy, Gradle and version catalogs, the manifest, the Activity lifecycle, resources, intents and Logcat." },
    { id: "views", file: "views.html", num: "19", group: "Android with Java", level: "Intermediate", minutes: 75,
      title: "Building UIs with Views", short: "Views & Layouts", icon: "🎨", accent: "#3ddc84",
      desc: "XML layouts, ConstraintLayout, Material components, View Binding, RecyclerView with ListAdapter, dialogs and themes." },
    { id: "architecture", file: "architecture.html", num: "20", group: "Android with Java", level: "Advanced", minutes: 70,
      title: "App Architecture: ViewModel, LiveData & Repository", short: "App Architecture", icon: "🏗️", accent: "#3ddc84",
      desc: "Layers, ViewModel, LiveData and MediatorLiveData, unidirectional data flow, repositories, background executors and manual DI." },
    { id: "data", file: "data.html", num: "21", group: "Android with Java", level: "Advanced", minutes: 70,
      title: "Data, Networking & Background Work", short: "Data & Networking", icon: "🗄️", accent: "#3ddc84",
      desc: "Room, SharedPreferences, HTTP with HttpURLConnection and Retrofit, JSON with Gson, notifications and WorkManager." },
    { id: "release", file: "release.html", num: "22", group: "Android with Java", level: "Master", minutes: 50,
      title: "Testing, Debugging & Publishing", short: "Test, Debug & Ship", icon: "🚀", accent: "#3ddc84",
      desc: "Local and instrumented tests, Espresso, Logcat, the debugger and profiler, R8, signing, App Bundles and Google Play." },
    { id: "final-project", file: "final-project.html", num: "23", group: "Final Project", level: "Master", minutes: 360,
      title: "Final Project: Spendly — a complete Android app in Java", short: "Final Project: Spendly", icon: "🏁", accent: "#ffcb6b",
      desc: "Build a production-style expense tracker with Views, ViewModel, LiveData, Room, WorkManager, networking and tests." },
    { id: "cheatsheet", file: "cheatsheet.html", num: "A", group: "Reference", level: "All", minutes: 15,
      title: "Java Cheat Sheet & Mastery Checklist", short: "Cheat Sheet", icon: "📋", accent: "#9a98a8",
      desc: "Syntax at a glance, the most-used APIs, Android Studio shortcuts and the checklist to call yourself a Java master." },
    { id: "best-practices", file: "best-practices.html", num: "B", group: "Reference", level: "All", minutes: 60,
      title: "Java Coding Rules & Best Practices", short: "Coding Rules & Best Practices", icon: "✅", accent: "#3ddc97",
      desc: "Formatting, naming, methods, classes, null, exceptions, collections, concurrency, comments and Android rules — each with a bad vs good example." },
  ];
  const LESSONS = SITE.filter((p) => p.group !== "Reference");

  const $ = (s, r = document) => r.querySelector(s);
  const $$ = (s, r = document) => Array.from(r.querySelectorAll(s));
  const esc = (s) => String(s).replace(/[&<>"']/g, (c) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c]));
  const norm = (s) => String(s).toLowerCase().normalize("NFD").replace(/[̀-ͯ]/g, "");

  const pageId = document.body.dataset.page || "home";
  const currentPage = SITE.find((p) => p.id === pageId);

  /* ------------------------------------------------------- Storage */
  const store = {
    get(k, d) { try { const v = localStorage.getItem(k); return v == null ? d : JSON.parse(v); } catch (e) { return d; } },
    set(k, v) { try { localStorage.setItem(k, JSON.stringify(v)); } catch (e) { /* storage unavailable */ } },
  };
  const doneSet = () => new Set(store.get("ja-done", []));
  function setDone(id, on) {
    const s = doneSet();
    on ? s.add(id) : s.delete(id);
    store.set("ja-done", Array.from(s));
    document.dispatchEvent(new CustomEvent("ja:progress"));
  }

  const ICON_SEARCH = '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round"><circle cx="11" cy="11" r="7"/><path d="m20 20-3.5-3.5"/></svg>';
  const LOGO = '<svg class="brand-mark" viewBox="0 0 64 64" aria-hidden="true"><defs><linearGradient id="jg" x1="0" y1="1" x2="1" y2="0"><stop offset="0" stop-color="#2d6fb8"/><stop offset=".55" stop-color="#e76f00"/><stop offset="1" stop-color="#f89820"/></linearGradient></defs><rect width="64" height="64" rx="14" fill="#16171b"/><path d="M15 30h30v6a13 13 0 0 1-13 13h-4a13 13 0 0 1-13-13z" fill="url(#jg)"/><path d="M45 33h3a5 5 0 0 1 0 10h-4" fill="none" stroke="url(#jg)" stroke-width="4" stroke-linecap="round"/><path d="M24 12c-3 4 3 6 0 10M32 10c-3 5 3 7 0 12" fill="none" stroke="#f89820" stroke-width="3" stroke-linecap="round"/><rect x="13" y="51" width="34" height="4" rx="2" fill="#2d6fb8"/></svg>';

  /* --------------------------------------------------------------- Header */
  function renderHeader() {
    const host = $("#site-header");
    if (!host) return;
    const links = LESSONS.map((p) => `<a href="${p.file}"><span class="dm-num">${p.num}</span><span><b>${esc(p.short)}</b><i>${esc(p.desc)}</i></span></a>`).join("");
    const nav = (id, label, file) => `<a href="${file}" class="${pageId === id ? "active" : ""}">${label}</a>`;
    host.outerHTML = `
      <header class="site-header">
        <button class="icon-btn menu-btn" id="menu-btn" aria-label="Open table of contents" aria-expanded="false">☰</button>
        <a class="brand" href="index.html" aria-label="Java Academy home">${LOGO}<span>Java Academy<small>Scratch → Master</small></span></a>
        <nav class="main-nav" aria-label="Main navigation">
          ${nav("home", "Home", "index.html")}
          <div class="nav-drop ${currentPage && currentPage.group !== "Reference" ? "active" : ""}">
            <button aria-haspopup="true">Lessons ▾</button>
            <div class="nav-drop-menu">${links}</div>
          </div>
          ${nav("setup", "Setup", "setup.html")}
          ${nav("android-basics", "Android", "android-basics.html")}
          ${nav("final-project", "Final Project", "final-project.html")}
          ${nav("cheatsheet", "Cheat Sheet", "cheatsheet.html")}
          ${nav("best-practices", "Best Practices", "best-practices.html")}
        </nav>
        <div class="header-spacer"></div>
        <div class="global-search" role="search">
          <span class="s-icon">${ICON_SEARCH}</span>
          <input id="gs-input" type="search" placeholder="Search lessons…" autocomplete="off" aria-label="Search the site">
          <kbd>Ctrl K</kbd>
          <div class="search-results" id="gs-results" role="listbox"></div>
        </div>
        <button class="icon-btn" id="theme-btn" type="button"></button>
      </header>`;
    const mb = $("#menu-btn"), sb = $("#sidebar");
    if (!sb) { mb.style.visibility = "hidden"; return; }
    mb.addEventListener("click", () => {
      const open = sb.classList.toggle("open");
      mb.setAttribute("aria-expanded", String(open));
    });
    sb.addEventListener("click", (e) => { if (e.target.closest("a")) sb.classList.remove("open"); });
  }

  /* ---------------------------------------------------------------- Theme */
  function initTheme() {
    const btn = $("#theme-btn");
    if (!btn) return;
    const root = document.documentElement;
    const media = window.matchMedia("(prefers-color-scheme: light)");
    const effective = () => root.getAttribute("data-theme") || (media.matches ? "light" : "dark");
    const paint = () => {
      const light = effective() === "light";
      btn.textContent = light ? "🌙" : "☀️";
      btn.setAttribute("aria-label", light ? "Switch to dark theme" : "Switch to light theme");
      btn.title = btn.getAttribute("aria-label");
    };
    btn.addEventListener("click", () => {
      const next = effective() === "light" ? "dark" : "light";
      root.setAttribute("data-theme", next);
      try { localStorage.setItem("ja-theme", next); } catch (e) { /* storage unavailable */ }
      paint();
    });
    media.addEventListener("change", paint);
    paint();
  }

  /* -------------------------------------------------------- Global search */
  /* The index is built by fetching every page and reading its sections, so
     search covers the full text without a hand-maintained keyword list. Over
     file:// fetch is blocked; we then fall back to page titles only. */
  function sectionsOf(doc, page) {
    return $$("section.doc-section[id]", doc).map((s) => {
      const h = $("h2", s);
      const title = s.dataset.title || (h ? h.textContent.replace(/^\s*[\d.]+\s*/, "") : s.id);
      // Join text nodes with spaces so words from adjacent elements don't run together.
      const walker = doc.createTreeWalker(s, NodeFilter.SHOW_TEXT);
      const parts = [];
      while (walker.nextNode()) parts.push(walker.currentNode.nodeValue);
      const text = parts.join(" ").replace(/\s+/g, " ");
      return { title, sub: page.short, page: page.num, href: `${page.file}#${s.id}`, text };
    });
  }
  let indexPromise = null;
  function loadIndex() {
    if (indexPromise) return indexPromise;
    const base = SITE.map((p) => ({ title: p.title, sub: p.desc, page: p.group === "Reference" ? "Ref " + p.num : "Lesson " + p.num, href: p.file, text: p.desc }));
    let cached = null;
    try { cached = JSON.parse(sessionStorage.getItem("ja-index-v1") || "null"); } catch (e) { /* ignore */ }
    if (cached) return (indexPromise = Promise.resolve(base.concat(cached)));
    indexPromise = Promise.all(SITE.map((p) =>
      fetch(p.file).then((r) => (r.ok ? r.text() : "")).then((html) => sectionsOf(new DOMParser().parseFromString(html, "text/html"), p)).catch(() => [])
    )).then((lists) => {
      const all = [].concat(...lists);
      try { sessionStorage.setItem("ja-index-v1", JSON.stringify(all)); } catch (e) { /* quota */ }
      return base.concat(all);
    }).catch(() => base);
    return indexPromise;
  }
  function hilite(text, q) {
    const n = norm(text), i = q ? n.indexOf(q) : -1;
    if (i < 0) return esc(text);
    return esc(text.slice(0, i)) + "<mark>" + esc(text.slice(i, i + q.length)) + "</mark>" + esc(text.slice(i + q.length));
  }
  function snippet(text, q) {
    const n = norm(text), i = n.indexOf(q);
    if (i < 0) return "";
    const s = Math.max(0, i - 50);
    return (s > 0 ? "…" : "") + text.slice(s, i + q.length + 70) + "…";
  }
  function initGlobalSearch() {
    const input = $("#gs-input"), box = $("#gs-results");
    if (!input) return;
    let sel = -1;
    const run = () => {
      const raw = input.value.trim(), q = norm(raw);
      if (!q) { box.classList.remove("open"); return; }
      loadIndex().then((index) => {
        if (input.value.trim() !== raw) return;
        const terms = q.split(/\s+/);
        const scored = index.map((e) => {
          const t = norm(e.title), body = norm(e.text);
          if (!terms.every((w) => t.includes(w) || body.includes(w))) return null;
          const score = terms.reduce((a, w) => a + (t.includes(w) ? 10 : 0) + (t.startsWith(w) ? 5 : 0), 0);
          return { e, score };
        }).filter(Boolean).sort((a, b) => b.score - a.score).slice(0, 14);
        sel = -1;
        box.innerHTML = scored.length
          ? scored.map(({ e }) => {
              const snip = norm(e.title).includes(terms[0]) ? e.sub : snippet(e.text, terms[0]) || e.sub;
              return `<a href="${e.href}" role="option"><span class="sr-page">${esc(e.page)}</span><br><b>${hilite(e.title, terms[0])}</b><small>${hilite(snip, terms[0])}</small></a>`;
            }).join("")
          : `<div class="sr-empty">No results for “${esc(raw)}”.</div>`;
        box.classList.add("open");
      });
    };
    input.addEventListener("input", run);
    input.addEventListener("focus", () => { loadIndex(); if (input.value) run(); });
    input.addEventListener("keydown", (e) => {
      const items = $$("a", box);
      if (e.key === "ArrowDown" || e.key === "ArrowUp") {
        e.preventDefault();
        if (!items.length) return;
        sel = (sel + (e.key === "ArrowDown" ? 1 : -1) + items.length) % items.length;
        items.forEach((a, i) => a.classList.toggle("sel", i === sel));
        items[sel].scrollIntoView({ block: "nearest" });
      } else if (e.key === "Enter") {
        const t = items[sel >= 0 ? sel : 0];
        if (t) location.href = t.getAttribute("href");
      } else if (e.key === "Escape") {
        box.classList.remove("open"); input.blur();
      }
    });
    document.addEventListener("click", (e) => { if (!e.target.closest(".global-search")) box.classList.remove("open"); });
    document.addEventListener("keydown", (e) => {
      if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === "k") { e.preventDefault(); input.focus(); input.select(); }
      else if (e.key === "/" && !/input|textarea|select/i.test(document.activeElement.tagName)) { e.preventDefault(); input.focus(); }
    });
  }

  /* --------------------------------------------------------------- Sidebar */
  function renderSidebar() {
    const host = $("#sidebar");
    if (!host) return;
    const done = doneSet();
    const groups = {};
    SITE.forEach((p) => (groups[p.group] = groups[p.group] || []).push(p));
    const sections = $$("section.doc-section[id]").map((s) => {
      const h = $("h2", s);
      return [s.id, s.dataset.title || (h ? h.textContent : s.id)];
    });
    let html = `
      <div class="sb-progress" title="Lessons you marked complete">
        <div class="sb-progress-label"><span>Course progress</span><span id="sb-course">0%</span></div>
        <div class="sb-progress-bar"><div id="sb-course-bar"></div></div>
      </div>
      <div class="sb-progress">
        <div class="sb-progress-label"><span>This page</span><span id="sb-pct">0%</span></div>
        <div class="sb-progress-bar"><div id="sb-bar"></div></div>
      </div>
      <nav aria-label="Table of contents">`;
    Object.entries(groups).forEach(([g, pages]) => {
      html += `<div class="sb-group"><div class="sb-group-title">${esc(g)}</div>`;
      pages.forEach((p) => {
        const cur = p.id === pageId;
        html += `<a class="sb-page ${cur ? "current" : ""}" href="${p.file}" ${cur ? 'aria-current="page"' : ""}><span class="sb-num">${p.num}</span><span>${esc(p.short)}</span>${done.has(p.id) ? '<span class="sb-done" title="Completed">✓</span>' : ""}</a>`;
        if (cur && sections.length) {
          html += `<ul class="sb-sections">${sections.map(([id, t]) => `<li><a href="#${id}" data-sec="${id}">${esc(t)}</a></li>`).join("")}</ul>`;
        }
      });
      html += `</div>`;
    });
    host.innerHTML = html + "</nav>";
    paintCourseProgress();
    const cur = $(".sb-page.current", host);
    if (cur) setTimeout(() => cur.scrollIntoView({ block: "center" }), 0);
  }
  function paintCourseProgress() {
    const done = doneSet();
    const n = LESSONS.filter((p) => done.has(p.id)).length;
    const pct = Math.round((n / LESSONS.length) * 100);
    $$("#sb-course, #hp-pct").forEach((el) => (el.textContent = `${pct}%`));
    $$("#sb-course-bar, #hp-bar").forEach((el) => (el.style.width = pct + "%"));
    const c = $("#hp-count");
    if (c) c.textContent = `${n} of ${LESSONS.length} lessons completed`;
  }

  /* ---------------------------------------------- Scrollspy & progress */
  function initScrollspy() {
    const secs = $$("section.doc-section[id]");
    const links = $$(".sb-sections a");
    const bar = $("#sb-bar"), pct = $("#sb-pct"), top = $(".to-top");
    const onScroll = () => {
      const y = window.scrollY + 140;
      let active = secs[0];
      secs.forEach((s) => { if (s.offsetTop <= y) active = s; });
      links.forEach((a) => a.classList.toggle("active", active && a.dataset.sec === active.id));
      const h = document.documentElement.scrollHeight - window.innerHeight;
      const p = h > 0 ? Math.min(100, Math.round((window.scrollY / h) * 100)) : 100;
      if (bar) bar.style.width = p + "%";
      if (pct) pct.textContent = p + "%";
      if (top) top.classList.toggle("show", window.scrollY > 900);
    };
    window.addEventListener("scroll", onScroll, { passive: true });
    onScroll();
  }
  function decorateSections() {
    $$("section.doc-section[id] .sec-head h2").forEach((h) => {
      const id = h.closest("section").id;
      if (!$(".anchor", h)) h.insertAdjacentHTML("beforeend", `<a class="anchor" href="#${id}" aria-label="Link to this section">#</a>`);
    });
  }

  /* --------------------------------------------- Lesson completion & pager */
  function renderPager() {
    const host = $("#pager");
    if (!host || !currentPage) return;
    const i = SITE.indexOf(currentPage);
    const prev = SITE[i - 1], next = SITE[i + 1];
    const isLesson = currentPage.group !== "Reference";
    host.innerHTML = `
      ${isLesson ? `<div class="lesson-done"><p><b>Finished this lesson?</b><br><span class="muted small">Your progress is saved in this browser only.</span></p><button class="btn" id="done-btn" type="button"></button></div>` : ""}
      <div class="pager">
        ${prev ? `<a class="prev" href="${prev.file}"><small>← Previous · ${prev.num}</small><b>${esc(prev.short)}</b></a>` : "<span></span>"}
        ${next ? `<a class="next" href="${next.file}"><small>Next · ${next.num} →</small><b>${esc(next.short)}</b></a>` : ""}
      </div>`;
    const btn = $("#done-btn");
    if (!btn) return;
    const paint = () => {
      const on = doneSet().has(pageId);
      btn.className = on ? "btn done" : "btn primary";
      btn.textContent = on ? "✓ Completed — undo" : "Mark lesson complete";
    };
    btn.addEventListener("click", () => { setDone(pageId, !doneSet().has(pageId)); paint(); renderSidebar(); initScrollspy(); });
    paint();
  }

  /* --------------------------------------------------------------- Footer */
  function renderFooter() {
    const host = $("#site-footer");
    if (!host) return;
    host.outerHTML = `
      <footer class="site-footer"><div class="f-inner">
        <div><b style="color:var(--text)">Java Academy</b><br>Learn Java from scratch to master — and ship a real Android app.</div>
        <div class="f-links">
          <a href="https://dev.java/learn/" target="_blank" rel="noopener">dev.java ↗</a>
          <a href="https://docs.oracle.com/en/java/javase/21/docs/api/" target="_blank" rel="noopener">Java 21 API ↗</a>
          <a href="https://developer.android.com/guide" target="_blank" rel="noopener">Android guides ↗</a>
          <a href="https://developer.android.com/studio" target="_blank" rel="noopener">Android Studio ↗</a>
          <a href="cheatsheet.html">Cheat sheet</a>
        </div>
      </div></footer>
      <button class="icon-btn to-top" type="button" aria-label="Back to top" onclick="window.scrollTo({top:0})">↑</button>`;
  }

  /* ----------------------------------------------------------------- Tabs */
  /* <div class="tabs" data-group="os"><div class="tab-list"><button data-tab="win">…</button></div>
     <div class="tab-panel" data-panel="win">…</div></div>. Tabs sharing a data-group switch together. */
  function initTabs(root = document) {
    $$(".tabs", root).forEach((tabs) => {
      const list = $(".tab-list", tabs);
      const btns = $$(":scope > .tab-list > button", tabs);
      const panels = $$(":scope > .tab-panel", tabs);
      list.setAttribute("role", "tablist");
      const select = (key, fromGroup) => {
        if (!btns.some((b) => b.dataset.tab === key)) return;
        btns.forEach((b) => { const on = b.dataset.tab === key; b.setAttribute("aria-selected", String(on)); b.tabIndex = on ? 0 : -1; });
        panels.forEach((p) => (p.hidden = p.dataset.panel !== key));
        if (!fromGroup && tabs.dataset.group) {
          store.set("ja-tab-" + tabs.dataset.group, key);
          $$(`.tabs[data-group="${tabs.dataset.group}"]`).forEach((t) => t !== tabs && t._select && t._select(key, true));
        }
      };
      tabs._select = select;
      btns.forEach((b) => {
        b.setAttribute("role", "tab");
        b.type = "button";
        b.addEventListener("click", () => select(b.dataset.tab));
        b.addEventListener("keydown", (e) => {
          if (e.key !== "ArrowRight" && e.key !== "ArrowLeft") return;
          const i = btns.indexOf(b), n = btns[(i + (e.key === "ArrowRight" ? 1 : -1) + btns.length) % btns.length];
          select(n.dataset.tab); n.focus();
        });
      });
      let initial = btns[0] && btns[0].dataset.tab;
      if (tabs.dataset.group) {
        const saved = store.get("ja-tab-" + tabs.dataset.group, null);
        if (saved && btns.some((b) => b.dataset.tab === saved)) initial = saved;
        else if (tabs.dataset.group === "os") {
          const ua = navigator.userAgent;
          const guess = /Mac/.test(ua) ? "mac" : /Linux/.test(ua) && !/Android/.test(ua) ? "linux" : "win";
          if (btns.some((b) => b.dataset.tab === guess)) initial = guess;
        }
      }
      if (initial) select(initial, true);
    });
  }

  /* ---------------------------------------------------------- Highlighter */
  const KT_KW = new Set(("as as? break class continue do else false for fun if in !in interface is !is null object package return super this throw true try typealias typeof val var when while " +
    "by catch constructor delegate dynamic field file finally get import init param property receiver set setparam value where " +
    "abstract actual annotation companion const crossinline data enum expect external final infix inline inner internal lateinit noinline open operator out override private protected public reified sealed suspend tailrec vararg it").split(" "));
  const LANG_NAMES = { kotlin: "Kotlin", kts: "Gradle KTS", java: "Java", xml: "XML", bash: "Shell", shell: "Shell", powershell: "PowerShell", toml: "TOML", json: "JSON", properties: "Properties", text: "Text", proguard: "ProGuard", groovy: "Groovy" };
  const JAVA_KW = new Set("abstract assert boolean break byte case catch char class const continue default do double else enum extends final finally float for goto if implements import instanceof int interface long native new package private protected public return short static strictfp super switch synchronized this throw throws transient try void volatile while var record sealed permits non yield when null true false".split(" "));

  function tokenize(code, lang) {
    const out = [];
    const push = (cls, t) => out.push([cls, t]);
    if (lang === "xml") {
      const re = /(<!--[\s\S]*?-->)|(<\/?[\w:.-]+)|(\s[\w:.-]+)(=)|("[^"]*")|(\/?>)|([^<"]+|.)/g;
      let m;
      while ((m = re.exec(code))) {
        if (m[1]) push("com", m[1]);
        else if (m[2]) push("tag", m[2]);
        else if (m[3]) { push("attr", m[3]); push("", m[4]); }
        else if (m[5]) push("str", m[5]);
        else if (m[6]) push("tag", m[6]);
        else push("", m[7]);
      }
      return out;
    }
    if (lang === "bash" || lang === "shell" || lang === "powershell") {
      code.split("\n").forEach((line, li) => {
        if (li) push("", "\n");
        const pm = line.match(/^(\s*(?:\$|PS>|>|#)\s)(.*)$/);
        let rest = line;
        if (pm && !/^\s*#\s/.test(line)) { push("prompt", pm[1]); rest = pm[2]; }
        const re = /(#.*$)|("(?:[^"\\]|\\.)*"|'[^']*')|(\s--?[\w-]+)|(\$\{?\w+\}?|%\w+%|\$env:\w+)|([^#"'$%\s-]+|\s+|.)/g;
        let m;
        while ((m = re.exec(rest))) {
          if (m[1]) push("com", m[1]);
          else if (m[2]) push("str", m[2]);
          else if (m[3]) push("attr", m[3]);
          else if (m[4]) push("tmpl", m[4]);
          else push("", m[5]);
        }
      });
      return out;
    }
    if (lang === "toml" || lang === "properties") {
      const re = /(#.*$)|(^\s*\[[^\]]*\])|("(?:[^"\\]|\\.)*")|(^\s*[\w.-]+(?=\s*=))|(\b\d[\w.]*\b|\btrue\b|\bfalse\b)|([\s\S])/gm;
      let m;
      while ((m = re.exec(code))) {
        if (m[1]) push("com", m[1]);
        else if (m[2]) push("kw", m[2]);
        else if (m[3]) push("str", m[3]);
        else if (m[4]) push("attr", m[4]);
        else if (m[5]) push("num", m[5]);
        else push("", m[6]);
      }
      return out;
    }
    if (lang === "json") {
      const re = /("(?:[^"\\]|\\.)*")(\s*:)?|(-?\b\d[\d.eE+-]*\b|\btrue\b|\bfalse\b|\bnull\b)|([\s\S])/g;
      let m;
      while ((m = re.exec(code))) {
        if (m[1]) { push(m[2] ? "attr" : "str", m[1]); if (m[2]) push("", m[2]); }
        else if (m[3]) push("num", m[3]);
        else push("", m[4]);
      }
      return out;
    }
    if (lang === "text") { push("", code); return out; }
    if (lang === "proguard") lang = "java";
    // Kotlin / KTS / Java / Groovy / ProGuard (C-like)
    const kw = lang === "java" || lang === "groovy" ? JAVA_KW : KT_KW;
    const re = /(\/\*[\s\S]*?\*\/|\/\/.*$)|("""[\s\S]*?"""|"(?:[^"\\\n]|\\.)*"|'(?:[^'\\\n]|\\.)*')|(@[\w.]+)|(\b0[xX][\da-fA-F_]+[uUL]*\b|\b\d[\d_]*(?:\.\d[\d_]*)?(?:[eE][+-]?\d+)?[fFLuU]*\b)|(`[^`]+`|[A-Za-z_][\w]*)|(\s+)|([\s\S])/gm;
    let m, prevWord = "";
    while ((m = re.exec(code))) {
      if (m[1]) push("com", m[1]);
      else if (m[2]) {
        if ((lang === "kotlin" || lang === "kts") && m[2][0] === '"') pushTemplate(out, m[2]);
        else push("str", m[2]);
      } else if (m[3]) push("ann", m[3]);
      else if (m[4]) push("num", m[4]);
      else if (m[5]) {
        const w = m[5];
        const after = code.slice(re.lastIndex).match(/^\s*(\(|<[\w<>, ?*.]*>\s*\()/);
        if (kw.has(w)) push("kw", w);
        else if (prevWord === "fun" || (after && /^[a-z_]/.test(w) && prevWord !== "new")) push("fn", w);
        else if (/^[A-Z][A-Za-z0-9_]*$/.test(w) && /[a-z]/.test(w)) push("type", w);
        else push("", w);
        prevWord = w;
        continue;
      } else if (m[6]) { push("", m[6]); continue; }
      else push("", m[7]);
      prevWord = "";
    }
    return out;
  }
  /* "Hello, $name and ${user.age}" -> string pieces + template pieces */
  function pushTemplate(out, s) {
    const re = /(\$\{[^}]*\}|\$[A-Za-z_]\w*)|([^$]+|\$)/g;
    let m;
    while ((m = re.exec(s))) out.push(m[1] ? ["tmpl", m[1]] : ["str", m[2]]);
  }
  function toLines(tokens) {
    const lines = [[]];
    tokens.forEach(([cls, t]) => {
      t.split("\n").forEach((part, i) => {
        if (i) lines.push([]);
        if (part) lines[lines.length - 1].push(cls ? `<span class="tk-${cls}">${esc(part)}</span>` : esc(part));
      });
    });
    return lines.map((l) => l.join(""));
  }
  function dedent(src) {
    const lines = src.replace(/^\s*\n/, "").replace(/\s+$/, "").split("\n");
    const ind = Math.min(...lines.filter((l) => l.trim()).map((l) => l.match(/^ */)[0].length));
    return lines.map((l) => l.slice(ind)).join("\n");
  }
  /* Line markers: "//+" add, "//-" del, "//!" highlight — at the end of a line, stripped from output. */
  function parseMarks(code) {
    const marks = {};
    const clean = code.split("\n").map((l, i) => {
      const m = l.match(/\s*\/\/([+\-!])\s*$/) || l.match(/\s*#([+\-!])\s*$/);
      if (m) { marks[i] = { "+": "add", "-": "del", "!": "hl" }[m[1]]; return l.slice(0, m.index); }
      return l;
    }).join("\n");
    return { code: clean, marks };
  }
  function codeBlockHTML({ code, lang, file, marks = {}, numbered, run }) {
    const lines = toLines(tokenize(code, lang));
    const body = lines.map((l, i) => `<span class="ln${marks[i] ? " " + marks[i] : ""}" data-n="${i + 1}">${l}</span>`).join("");
    return `<div class="code-head"><span class="dots"><i></i><i></i><i></i></span>${file ? `<span class="fname">${esc(file)}</span>` : ""}<span class="lang">${esc(LANG_NAMES[lang] || lang)}</span>${run ? `<button class="code-btn run" type="button" data-run title="Edit and run this code (Ctrl+Enter)">▶ Run</button>` : ""}<button class="code-btn" type="button" data-copy>Copy</button></div><pre><code>${body}</code></pre>`;
  }
  /* <script type="text/plain" data-code="java" data-file="Main.java" data-run data-stdin="Ada\n36" data-numbered>  */
  function renderCode(root = document) {
    $$('script[type="text/plain"][data-code]', root).forEach((s) => {
      const lang = s.dataset.code || "java";
      const raw = dedent(s.textContent).replace(/<\\\/script>/g, "</script>");
      const { code, marks } = parseMarks(raw);
      const wrap = document.createElement("div");
      const hasOut = s.nextElementSibling && s.nextElementSibling.matches("script[data-output]");
      wrap.className = "code-block" + ("numbered" in s.dataset ? " numbered" : "") + (hasOut ? " has-output" : "");
      wrap._code = code;
      wrap._stdin = s.dataset.stdin ? s.dataset.stdin.replace(/\\n/g, "\n") : "";
      wrap.innerHTML = codeBlockHTML({ code, lang, file: s.dataset.file, marks, run: "run" in s.dataset });
      s.replaceWith(wrap);
    });
    $$("script[data-output]", root).forEach((s) => {
      const d = document.createElement("div");
      d.className = "code-output";
      d.innerHTML = `<div class="oh">▸ ${esc(s.dataset.output || "Output")}</div><pre>${esc(dedent(s.textContent))}</pre>`;
      s.replaceWith(d);
    });
  }
  function copyText(text, done) {
    if (navigator.clipboard && window.isSecureContext) navigator.clipboard.writeText(text).then(done, () => fallbackCopy(text, done));
    else fallbackCopy(text, done);
  }
  function fallbackCopy(text, done) {
    const ta = document.createElement("textarea");
    ta.value = text; ta.style.position = "fixed"; ta.style.opacity = "0";
    document.body.appendChild(ta); ta.select();
    try { document.execCommand("copy"); done(); } catch (e) { /* ignore */ }
    ta.remove();
  }

  /* ----------------------------------------------- Live Java editor (Run) */
  /* ▶ Run turns a block into an editable textarea and executes it on the free
     Compiler Explorer API (godbolt.org), which compiles with a real JDK and runs
     main(). If the service is unreachable we copy the code and point learners
     at Android Studio instead — every runnable example is also verified in CI. */
  const RUNNER = "https://godbolt.org/api";
  let compilerPromise = null;
  function pickCompiler() {
    if (compilerPromise) return compilerPromise;
    const ver = (s) => String(s || "").split(/\D+/).filter(Boolean).map(Number);
    const cmp = (a, b) => {
      const x = ver(a.semver), y = ver(b.semver);
      for (let i = 0; i < Math.max(x.length, y.length); i++) { const d = (x[i] || 0) - (y[i] || 0); if (d) return d; }
      return 0;
    };
    compilerPromise = fetch(RUNNER + "/compilers/java?fields=id,name,semver", { headers: { Accept: "application/json" } })
      .then((r) => (r.ok ? r.json() : Promise.reject(new Error("HTTP " + r.status))))
      .then((list) => {
        const stable = list.filter((c) => !/graal|j9|trunk|head|snapshot|\bea\b/i.test(`${c.id} ${c.name}`) && ver(c.semver)[0] >= 21);
        const pool = (stable.length ? stable : list).slice().sort(cmp);
        if (!pool.length) throw new Error("no Java compiler");
        return pool[pool.length - 1];
      })
      .catch((e) => { compilerPromise = null; throw e; });
    return compilerPromise;
  }
  const textOf = (a) => (Array.isArray(a) ? a.map((x) => x.text).join("\n") : "").replace(/\x1b\[[\d;]*m/g, "");
  function executeJava(code, stdin) {
    // The service compiles a file called example.java, so a top-level type may not be public.
    const src = code.replace(/^public\s+(?=(?:(?:final|abstract|sealed|non-sealed|strictfp)\s+)*(?:class|interface|enum|record)\b)/gm, "");
    return pickCompiler().then((c) => fetch(`${RUNNER}/compiler/${encodeURIComponent(c.id)}/compile`, {
      method: "POST",
      headers: { "Content-Type": "application/json", Accept: "application/json" },
      body: JSON.stringify({
        source: src, lang: "java", allowStoreCodeDebug: false,
        options: {
          userArguments: "", executeParameters: { args: [], stdin: stdin || "" },
          compilerOptions: { executorRequest: true, skipAsm: true }, filters: { execute: true }, tools: [], libraries: [],
        },
      }),
    }).then((r) => (r.ok ? r.json() : Promise.reject(new Error("HTTP " + r.status))))
      .then((res) => {
        const ex = res.execResult || res;
        const build = ex.buildResult || {};
        const built = ex.didExecute !== false && !(build.code > 0);
        if (!built) return { ok: false, compiler: c.name, text: textOf(build.stderr) || textOf(build.stdout) || textOf(res.stderr) || "Compilation failed." };
        const out = textOf(ex.stdout), err = textOf(ex.stderr);
        return { ok: !ex.code, compiler: c.name, exit: ex.code, text: out + (err ? (out ? "\n" : "") + err : "") };
      }));
  }
  function openEditor(block) {
    if (block._editor) return block._editor;
    const pre = $("pre", block);
    const ta = document.createElement("textarea");
    ta.className = "code-edit";
    ta.spellcheck = false;
    ta.setAttribute("aria-label", "Editable Java code — Ctrl+Enter runs it");
    ta.value = block._code;
    ta.rows = Math.min(40, block._code.split("\n").length + 1);
    pre.replaceWith(ta);
    block.classList.add("editing");
    ta.addEventListener("keydown", (e) => {
      if (e.key === "Tab" && !e.shiftKey) {
        e.preventDefault();
        const s = ta.selectionStart;
        ta.setRangeText("    ", s, ta.selectionEnd, "end");
      } else if (e.key === "Enter" && (e.ctrlKey || e.metaKey)) {
        e.preventDefault();
        runBlock(block, $("[data-run]", block));
      }
    });
    const head = $(".code-head", block);
    head.insertAdjacentHTML("beforeend", '<button class="code-btn" type="button" data-reset-code title="Restore the original example">Reset</button>');
    let stdinBox = null;
    if (/System\.in\b/.test(block._code)) {
      block.insertAdjacentHTML("beforeend", '<div class="code-stdin"><label>Input (stdin)</label><textarea spellcheck="false" rows="2"></textarea></div>');
      stdinBox = $(".code-stdin textarea", block);
      stdinBox.value = block._stdin || "";
    }
    // Replace a static "expected output" panel with a live one.
    let out = block.nextElementSibling && block.nextElementSibling.classList.contains("code-output") ? block.nextElementSibling : null;
    if (!out) { out = document.createElement("div"); block.after(out); }
    out.className = "code-output live";
    out.innerHTML = '<div class="oh"><span>▸ Output</span><span class="os"></span></div><pre></pre>';
    block.classList.add("has-output");
    block._editor = { ta, out, stdinBox };
    return block._editor;
  }
  function runBlock(block, btn) {
    const ed = openEditor(block);
    const code = ed.ta.value;
    btn.disabled = true;
    btn.textContent = "Running…";
    ed.out.classList.add("busy");
    ed.out.classList.remove("err");
    $(".os", ed.out).textContent = "compiling…";
    const t0 = performance.now();
    const done = () => { btn.disabled = false; btn.textContent = "▶ Run"; ed.out.classList.remove("busy"); };
    executeJava(code, ed.stdinBox ? ed.stdinBox.value : "").then((r) => {
      done();
      ed.out.classList.toggle("err", !r.ok);
      $(".os", ed.out).textContent = `${r.ok ? "" : r.exit != null ? "exit code " + r.exit + " · " : "compile error · "}${r.compiler} · ${((performance.now() - t0) / 1000).toFixed(1)} s`;
      $("pre", ed.out).textContent = r.text || "(no output)";
    }).catch(() => {
      done();
      ed.out.classList.add("err");
      $(".os", ed.out).textContent = "runner unavailable";
      copyText(code, () => {});
      $("pre", ed.out).innerHTML = "Couldn't reach the online Java runner (Compiler Explorer).\nThe code is copied to your clipboard — paste it into a class in your Android Studio\n<b>java-lessons</b> module (see <a href=\"setup.html#java-module\">lesson 01</a>) and press ▶ in the gutter,\nor try it at <a href=\"https://dev.java/playground/\" target=\"_blank\" rel=\"noopener\">dev.java/playground</a>.";
    });
  }
  function wireCodeButtons() {
    document.addEventListener("click", (e) => {
      const copy = e.target.closest("[data-copy]");
      if (copy) {
        const block = copy.closest(".code-block");
        copyText(block._editor ? block._editor.ta.value : block._code || $("pre", block).textContent, () => {
          copy.textContent = "Copied ✓"; copy.classList.add("ok");
          setTimeout(() => { copy.textContent = "Copy"; copy.classList.remove("ok"); }, 1600);
        });
        return;
      }
      const run = e.target.closest("[data-run]");
      if (run && run.classList.contains("code-btn")) { runBlock(run.closest(".code-block"), run); return; }
      const reset = e.target.closest("[data-reset-code]");
      if (reset) { const b = reset.closest(".code-block"); if (b._editor) b._editor.ta.value = b._code; }
    });
  }

  /* ----------------------------------------------------------------- Quiz */
  /* <script type="application/json" data-quiz>[{"q":"…","options":["…"],"answer":1,"explain":"…"}]</script>
     Options/explanations may contain `backticks` for inline code. */
  const inline = (s) => esc(s).replace(/`([^`]+)`/g, "<code>$1</code>");
  function renderQuizzes() {
    $$("script[data-quiz]").forEach((s, qi) => {
      let data;
      try { data = JSON.parse(s.textContent); } catch (e) { console.error("Bad quiz JSON", e); return; }
      const host = document.createElement("div");
      host.className = "quiz";
      const key = `ja-quiz-${pageId}-${qi}`;
      host.innerHTML = data.map((q, i) => `
        <div class="quiz-q" data-i="${i}">
          <h4><span class="qn">Q${i + 1}</span><span>${inline(q.q)}</span></h4>
          ${q.code ? codeBlockHTMLWrapped(q.code) : ""}
          <div class="quiz-opts">${q.options.map((o, j) => `<button type="button" data-j="${j}">${inline(o)}</button>`).join("")}</div>
          <div class="quiz-explain" hidden></div>
        </div>`).join("") + `<div class="quiz-score"><span class="muted">Score</span><b data-score>0 / ${data.length}</b><button class="btn sm" type="button" data-reset>Reset quiz</button></div>`;
      s.replaceWith(host);
      const answers = {};
      const paintScore = () => {
        const right = Object.entries(answers).filter(([i, j]) => data[i].answer === j).length;
        $("[data-score]", host).textContent = `${right} / ${data.length}`;
        store.set(key, answers);
      };
      const choose = (i, j) => {
        const box = $(`.quiz-q[data-i="${i}"]`, host);
        answers[i] = j;
        $$("button", box).forEach((b) => {
          b.disabled = true;
          const bj = +b.dataset.j;
          if (bj === data[i].answer) b.classList.add("right");
          else if (bj === j) b.classList.add("wrong");
        });
        const ex = $(".quiz-explain", box);
        ex.innerHTML = (j === data[i].answer ? "✅ <b>Correct.</b> " : "❌ <b>Not quite.</b> ") + inline(data[i].explain || "");
        ex.hidden = false;
        paintScore();
      };
      host.addEventListener("click", (e) => {
        const b = e.target.closest(".quiz-opts button");
        if (b) choose(+b.closest(".quiz-q").dataset.i, +b.dataset.j);
        if (e.target.closest("[data-reset]")) {
          Object.keys(answers).forEach((k) => delete answers[k]);
          $$(".quiz-opts button", host).forEach((b) => { b.disabled = false; b.className = ""; });
          $$(".quiz-explain", host).forEach((x) => (x.hidden = true));
          paintScore();
        }
      });
      const saved = store.get(key, {});
      Object.entries(saved).forEach(([i, j]) => data[i] && choose(+i, j));
    });
  }
  function codeBlockHTMLWrapped(code) {
    return `<div class="code-block">${codeBlockHTML({ code: dedent(code), lang: "java" })}</div>`;
  }

  /* ------------------------------------------------------------- Landing */
  function renderModuleGrid() {
    const host = $("#module-grid");
    if (!host) return;
    const done = doneSet();
    const groups = {};
    SITE.forEach((p) => (groups[p.group] = groups[p.group] || []).push(p));
    const lvl = { Beginner: "green", Intermediate: "blue", Advanced: "amber", Master: "red", All: "" };
    host.innerHTML = Object.entries(groups).map(([g, pages]) => `
      <div class="mod-group-title"><h3>${esc(g)}</h3></div>
      <div class="grid grid-3">${pages.map((p) => `
        <a class="module-card glass ${done.has(p.id) ? "is-done" : ""}" href="${p.file}" style="--accent:${p.accent}">
          <div class="mc-top"><div class="mc-icon">${p.icon}</div><span class="badge ${lvl[p.level]}">${p.group === "Reference" ? "Ref " + p.num : p.num}</span></div>
          <h3>${esc(p.title)}</h3>
          <p>${esc(p.desc)}</p>
          <div class="mc-foot"><span>⏱ ${p.minutes >= 120 ? Math.round(p.minutes / 60) + " h" : p.minutes + " min"}</span><span>· ${esc(p.level)}</span><span class="mc-go">Open →</span></div>
        </a>`).join("")}</div>`).join("");
    const cont = $("#continue-btn");
    if (cont) {
      const next = LESSONS.find((p) => !done.has(p.id));
      if (done.size && next) { cont.href = next.file; cont.textContent = `Continue: ${next.num} ${next.short} →`; }
    }
  }

  /* ------------------------------------------------------------------ Boot */
  function boot() {
    renderHeader();
    renderSidebar();
    renderFooter();
    initTheme();
    initGlobalSearch();
    renderCode();
    wireCodeButtons();
    renderQuizzes();
    initTabs();
    decorateSections();
    renderPager();
    renderModuleGrid();
    paintCourseProgress();
    initScrollspy();
    document.addEventListener("ja:progress", paintCourseProgress);
    if (location.hash) {
      const t = document.getElementById(decodeURIComponent(location.hash.slice(1)));
      if (t) setTimeout(() => t.scrollIntoView(), 0);
    }
  }

  window.JA = { SITE, LESSONS, esc, $, $$, store, setDone, doneSet };
  if (document.readyState === "loading") document.addEventListener("DOMContentLoaded", boot);
  else boot();
})();
