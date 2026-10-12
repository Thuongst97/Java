#!/usr/bin/env python3
"""Compile and run every runnable Java example in the Java Academy site.

A code block is checked when it is marked runnable:
    <script type="text/plain" data-code="java" data-run> ... </script>
If the next element is an expected-output block
    <script type="text/plain" data-output> ... </script>
the program's output must match it exactly (trailing whitespace ignored),
unless the output block carries `data-loose` (timing/random/thread-order output).
A `data-stdin="Ada\\n36"` attribute on the code block is fed to standard input.

Each snippet is written to its own directory (the file is named after its
public top-level class), compiled with javac and run with java, in parallel.

Usage:
    python3 tools/check_snippets.py [--release 21] [PAGE.html ...]
"""
import argparse
import concurrent.futures
import glob
import html.parser
import os
import re
import shutil
import subprocess
import sys
import tempfile
import textwrap

SITE = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "java-academy")


class Extractor(html.parser.HTMLParser):
    """Collects <script type=text/plain> blocks in document order."""

    def __init__(self):
        super().__init__(convert_charrefs=False)
        self.blocks = []
        self._cur = None

    def handle_starttag(self, tag, attrs):
        if tag != "script":
            return
        a = dict(attrs)
        if a.get("type") != "text/plain":
            return
        self._cur = {"attrs": a, "text": "", "line": self.getpos()[0]}

    def handle_data(self, data):
        if self._cur is not None:
            self._cur["text"] += data

    def handle_endtag(self, tag):
        if tag == "script" and self._cur is not None:
            self.blocks.append(self._cur)
            self._cur = None


MARK_RE = re.compile(r"\s*//[+\-!]\s*$")
PUBLIC_TYPE = re.compile(r"^public\s+(?:(?:final|abstract|sealed|non-sealed|strictfp)\s+)*(?:class|interface|enum|record)\s+(\w+)", re.M)
ANY_TYPE = re.compile(r"^(?:(?:final|abstract|sealed|non-sealed|strictfp)\s+)*(?:class|interface|enum|record)\s+(\w+)", re.M)
NOISE = ("Picked up JAVA_TOOL_OPTIONS", "Picked up _JAVA_OPTIONS")


def clean(src):
    src = textwrap.dedent(src.strip("\n")).rstrip()
    return "\n".join(MARK_RE.sub("", line) for line in src.split("\n"))


def strip_noise(text):
    return "\n".join(l for l in text.splitlines() if not l.startswith(NOISE))


def collect(pages):
    snippets = []
    for page in pages:
        ex = Extractor()
        with open(page, encoding="utf-8") as f:
            ex.feed(f.read())
        blocks = ex.blocks
        for i, b in enumerate(blocks):
            a = b["attrs"]
            if a.get("data-code", "java") != "java" or "data-run" not in a:
                continue
            expected, loose = None, False
            if i + 1 < len(blocks) and "data-output" in blocks[i + 1]["attrs"]:
                expected = textwrap.dedent(blocks[i + 1]["text"].strip("\n")).rstrip()
                loose = "data-loose" in blocks[i + 1]["attrs"]
            snippets.append({
                "page": os.path.basename(page), "line": b["line"], "code": clean(b["text"]),
                "stdin": a.get("data-stdin", "").replace("\\n", "\n"), "file": a.get("data-file", ""),
                "expected": expected, "loose": loose,
            })
    return snippets


def main_class(code):
    """The class whose main() we launch: the public one, else the first type that declares main."""
    m = PUBLIC_TYPE.search(code)
    if m:
        return m.group(1)
    names = ANY_TYPE.findall(code)
    for name in names:
        body = code[code.index(name):]
        if re.search(r"\bstatic\s+void\s+main\s*\(", body):
            return name
    return names[0] if names else "Main"


def check(s, work, release, timeout):
    d = os.path.join(work, f"s{s['n']}")
    os.makedirs(d)
    name = main_class(s["code"])
    shown = os.path.basename(s["file"].split(" ")[0])
    public = PUBLIC_TYPE.search(s["code"])
    if shown.endswith(".java") and public and shown != public.group(1) + ".java":
        return "file name mismatch", f"data-file says {shown} but the public class is {public.group(1)}"
    with open(os.path.join(d, name + ".java"), "w", encoding="utf-8") as f:
        f.write(s["code"] + "\n")
    c = subprocess.run(["javac", "--release", str(release), "-nowarn", "-encoding", "UTF-8", "-d", d, os.path.join(d, name + ".java")],
                       capture_output=True, text=True)
    if c.returncode != 0:
        return "compile error", strip_noise(c.stdout + c.stderr)
    try:
        r = subprocess.run(["java", "-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8", "-cp", d, name],
                           input=s["stdin"], capture_output=True, text=True, timeout=timeout, cwd=d)
    except subprocess.TimeoutExpired:
        return "timed out", ""
    out = strip_noise(r.stdout + (r.stderr if r.returncode else "")).rstrip()
    if r.returncode != 0 and (s["expected"] is None or s["expected"].strip() not in out):
        return f"exit code {r.returncode}", out
    if s["expected"] is not None and not s["loose"]:
        norm = lambda t: "\n".join(l.rstrip() for l in t.strip().splitlines())
        if norm(out) != norm(s["expected"]):
            return "output mismatch", out
    return None, out


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--release", type=int, default=21)
    ap.add_argument("--timeout", type=int, default=20)
    ap.add_argument("--jobs", type=int, default=os.cpu_count() or 4)
    ap.add_argument("pages", nargs="*")
    args = ap.parse_args()
    pages = args.pages or sorted(glob.glob(os.path.join(SITE, "*.html")))
    snippets = collect(pages)
    print(f"Found {len(snippets)} runnable snippets in {len(pages)} pages")
    work = tempfile.mkdtemp(prefix="ja-snippets-")
    for n, s in enumerate(snippets):
        s["n"] = n
    failed = 0
    with concurrent.futures.ThreadPoolExecutor(args.jobs) as pool:
        results = list(pool.map(lambda s: check(s, work, args.release, args.timeout), snippets))
    for s, (problem, out) in zip(snippets, results):
        if problem:
            failed += 1
            print(f"\n✗ {problem.upper()} {s['page']}:{s['line']}")
            if problem != "compile error":
                print("  expected:\n" + textwrap.indent(s["expected"] or "<none>", "    | "))
            print("  actual:\n" + textwrap.indent(out or "<empty>", "    | "))
    shutil.rmtree(work, ignore_errors=True)
    print(f"\n{len(snippets) - failed}/{len(snippets)} snippets passed")
    return 1 if failed else 0


if __name__ == "__main__":
    sys.exit(main())
