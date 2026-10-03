# TraceFinder

A command-line tool that analyses an access log file, scores each entry against a configurable rulebook, and writes a report flagging suspicious activity for human review.

Built by Abigael Gikunju and Erick Micheni.

## Prerequisites

- **Java 21 (JDK)** — check with `java -version`. Any JDK 21 distribution works (Oracle, Eclipse Temurin, etc.).
- **Maven** — check with `mvn -version`. Maven manages dependencies and the build; you don't need to install anything else by hand.

If either command isn't recognised, make sure `JAVA_HOME` and Maven's `bin` folder are on your system `PATH`.

## Setup

Clone the repository and move into the project folder:

```
git clone https://gitea.kood.tech/abigaelgikunju/trace-finder.git
cd trace-finder
```

No further setup is required — Maven will automatically download all dependencies (JUnit 5, etc.) the first time you build.

## Building the tool

```
mvn package
```

This compiles the source, runs the test suite, and produces a runnable jar at:

```
target/tracefinder-1.0-SNAPSHOT.jar
```

## Running the tool

The tool takes exactly three arguments, in this order: the log file, the rulebook CSV, and the path to write the report to.

```
java -jar target/tracefinder-1.0-SNAPSHOT.jar <logFile> <rulebookFile> <outputFile>
```

Example, using the sample files included in this repo:

```
java -jar target/tracefinder-1.0-SNAPSHOT.jar logs.txt rules.csv report.txt
```

On success, this prints a confirmation and writes the report to the path you gave. On failure (wrong number of arguments, a missing/unreadable input file, or an unwritable output path), it prints a clear error message and exits with a non-zero exit code.

### Input file formats

**Log file** — plain text, one entry per line, five `|`-separated fields:
```
timestamp | level | source IP | target | action
```

**Rulebook CSV** — a header row plus one row per level:
```
level,severity_score
INFO,1
WARN,3
ERROR,5
ALERT,9
```

## Running the tests

```
mvn test
```

This runs the full JUnit 5 test suite covering parsing, malformed-line handling, rulebook matching, analysis (counting, flagging, IP grouping), and report formatting. File I/O itself (reading the log/rulebook, writing the report) is intentionally not unit tested, since it's thin, directly-verifiable disk access — the logic layered on top of it is what's tested.

## Project structure

```
src/main/java/com/tracefinder/
  Main.java              — entry point; wires everything together and handles fatal errors
  model/                 — LogEntry, MalformedLine, UnknownPattern (data records)
  parser/                — LogParser, LogFileProcessor, LogFileReader (log reading + parsing)
  rulebook/               — RulebookLoader, RulebookMatcher (CSV loading + severity matching)
  analyzer/               — Analyzer, AnalysisResult, SuspiciousIp (the four findings)
  report/                 — ReportFormatter, ReportWriter (report text + disk output)

src/test/java/com/tracefinder/
  (mirrors the structure above — one test class per class under test)
```

## Architecture

Reading, processing, and writing are kept in separate packages that don't call each other directly. `Main` is the only class that calls into all of them, passing the result of one stage as input to the next:

```
read log file → parse lines → match against rulebook → analyze → format report → write report
```

Each stage that involves only data in/data out (parsing, matching, counting, flagging, formatting) is unit tested in isolation, without touching the filesystem. Stages that touch disk (`LogFileReader`, `RulebookLoader`'s file-reading half, `ReportWriter`) are thin and left untested, per the assignment's testing guidance.

### Error handling

- **Fatal errors** (wrong argument count; rulebook or log file missing/unreadable; rulebook not valid CSV; report path unwritable) print a specific message to stderr and exit with a non-zero code, stopping the program immediately.
- **Non-fatal issues** (malformed log lines; log levels not present in the rulebook) are recorded and the tool continues — they show up in the "Malformed Lines" and "Unknown Patterns" sections of the report rather than stopping the run.
