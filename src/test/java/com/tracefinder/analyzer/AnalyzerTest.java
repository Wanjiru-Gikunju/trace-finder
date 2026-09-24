package com.tracefinder.analyzer;

import com.tracefinder.model.LogEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnalyzerTest {

    private LogEntry entry(String level) {
        return new LogEntry(LocalDateTime.now(), level, "1.2.3.4", "/x", "y");
    }

    @Test
    void countsEntriesPerLevel() {
        Analyzer analyzer = new Analyzer();
        List<LogEntry> entries = List.of(
                entry("INFO"), entry("INFO"), entry("WARN"), entry("ALERT")
        );

        Map<String, Integer> summary = analyzer.buildActivitySummary(entries);

        assertEquals(2, summary.get("INFO"));
        assertEquals(1, summary.get("WARN"));
        assertEquals(1, summary.get("ALERT"));
    }

    @Test
void entryBelowThreeIsNotFlagged() {
    Analyzer analyzer = new Analyzer();
    LogEntry infoEntry = entry("INFO");
    Map<LogEntry, Integer> scored = Map.of(infoEntry, 1);

    List<LogEntry> flagged = analyzer.buildFlaggedEntries(scored);

    assertTrue(flagged.isEmpty());
}

@Test
void entryAtOrAboveThreeIsFlagged() {
    Analyzer analyzer = new Analyzer();
    LogEntry warnEntry = entry("WARN");
    Map<LogEntry, Integer> scored = Map.of(warnEntry, 3);

    List<LogEntry> flagged = analyzer.buildFlaggedEntries(scored);

    assertEquals(1, flagged.size());
    assertTrue(flagged.contains(warnEntry));
   }

}