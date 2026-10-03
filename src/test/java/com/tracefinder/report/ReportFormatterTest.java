package com.tracefinder.report;

import com.tracefinder.analyzer.AnalysisResult;
import com.tracefinder.analyzer.SuspiciousIp;
import com.tracefinder.model.LogEntry;
import com.tracefinder.model.MalformedLine;
import com.tracefinder.model.UnknownPattern;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReportFormatterTest {

    @Test
    void reportContainsAllFiveSectionsInOrder() {
        // Arrange
        ReportFormatter formatter = new ReportFormatter();

        Map<String, Integer> summary = Map.of("INFO", 2, "ALERT", 1);
        List<LogEntry> flagged = List.of(
                new LogEntry(LocalDateTime.of(2024, 3, 15, 2, 16, 2), "ALERT", "203.0.113.42", "/etc/passwd", "read")
        );
        List<SuspiciousIp> suspicious = List.of(new SuspiciousIp("203.0.113.42", 3));
        List<UnknownPattern> unknown = List.of(new UnknownPattern(89, "some raw content"));
        List<MalformedLine> malformed = List.of(new MalformedLine(23, "broken line"));

        AnalysisResult result = new AnalysisResult(summary, flagged, suspicious, unknown);
        LocalDateTime generatedAt = LocalDateTime.of(2024, 3, 15, 9, 0, 0);

        // Act
        String report = formatter.format(result, malformed, generatedAt);

        // Assert
        assertTrue(report.contains("=== Log Analysis Report ==="));
        assertTrue(report.contains("Generated: 2024-03-15 09:00:00"));
        assertTrue(report.contains("--- Activity Summary ---"));
        assertTrue(report.contains("--- Flagged Entries ---"));
        assertTrue(report.contains("--- Suspicious Activity by IP ---"));
        assertTrue(report.contains("--- Unknown Patterns ---"));
        assertTrue(report.contains("--- Malformed Lines ---"));
        assertTrue(report.contains("203.0.113.42: 3 entries"));
        assertTrue(report.contains("Line 89: some raw content"));
        assertTrue(report.contains("Line 23: broken line"));

        int summaryIndex = report.indexOf("--- Activity Summary ---");
        int flaggedIndex = report.indexOf("--- Flagged Entries ---");
        int suspiciousIndex = report.indexOf("--- Suspicious Activity by IP ---");
        int unknownIndex = report.indexOf("--- Unknown Patterns ---");
        int malformedIndex = report.indexOf("--- Malformed Lines ---");

        assertTrue(summaryIndex < flaggedIndex);
        assertTrue(flaggedIndex < suspiciousIndex);
        assertTrue(suspiciousIndex < unknownIndex);
        assertTrue(unknownIndex < malformedIndex);
    }

    @Test
    void emptySectionsShowNoneFoundMessage() {
        ReportFormatter formatter = new ReportFormatter();

        AnalysisResult result = new AnalysisResult(Map.of(), List.of(), List.of(), List.of());
        String report = formatter.format(result, List.of(), LocalDateTime.now());

        assertTrue(report.contains("None found"));
    }

    @Test
    void flaggedEntryPreservesItsOriginalSpacing() {
        String originalLine = "2024-03-15 02:16:02 | ALERT | 203.0.113.42  | /etc/passwd   | read";
        LogEntry entry = new LogEntry(LocalDateTime.of(2024, 3, 15, 2, 16, 2),
                "ALERT", "203.0.113.42", "/etc/passwd", "read", originalLine);
        AnalysisResult result = new AnalysisResult(Map.of(), List.of(entry), List.of(), List.of());

        String report = new ReportFormatter().format(result, List.of(), LocalDateTime.of(2024, 3, 15, 9, 0));

        assertTrue(report.contains(originalLine));
    }
}
