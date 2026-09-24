package com.tracefinder.rulebook;

import com.tracefinder.model.LogEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RulebookMatcherTest {

    private final Map<String, Integer> rulebook = Map.of(
            "INFO", 1,
            "WARN", 3,
            "ERROR", 5,
            "ALERT", 9
    );

    @Test
    void knownLevelGetsSeverityScore() {
        LogEntry entry = new LogEntry(LocalDateTime.now(), "WARN", "1.2.3.4", "/login", "failed");
        RulebookMatcher matcher = new RulebookMatcher();

        MatchResult result = matcher.match(entry, rulebook, 5);

        assertNotNull(result.getScoredEntry());
        assertEquals(3, result.getSeverityScore());
        assertNull(result.getUnknownPattern());
    }

    @Test
    void unrecognisedLevelBecomesUnknownPattern() {
        LogEntry entry = new LogEntry(LocalDateTime.now(), "CRIT", "1.2.3.4", "/admin", "escalated");
        RulebookMatcher matcher = new RulebookMatcher();

        MatchResult result = matcher.match(entry, rulebook, 89);

        assertNull(result.getScoredEntry());
        assertNotNull(result.getUnknownPattern());
        assertEquals(89, result.getUnknownPattern().lineNumber());
    }

    @Test
    void differentlyCasedLevelIsTreatedAsUnknown() {
        LogEntry entry = new LogEntry(LocalDateTime.now(), "info", "1.2.3.4", "/home", "accessed");
        RulebookMatcher matcher = new RulebookMatcher();

        MatchResult result = matcher.match(entry, rulebook, 12);

        assertNull(result.getScoredEntry());
        assertNotNull(result.getUnknownPattern());
    }
}