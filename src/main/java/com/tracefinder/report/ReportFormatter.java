package com.tracefinder.report;

import com.tracefinder.analyzer.AnalysisResult;
import com.tracefinder.analyzer.SuspiciousIp;
import com.tracefinder.model.LogEntry;
import com.tracefinder.model.MalformedLine;
import com.tracefinder.model.UnknownPattern;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ReportFormatter {

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String format(AnalysisResult result, List<MalformedLine> malformedLines, LocalDateTime generatedAt) {
        StringBuilder sb = new StringBuilder();

        sb.append("=== Log Analysis Report ===\n");
        sb.append("Generated: ").append(generatedAt.format(TIMESTAMP_FORMAT)).append("\n\n");

        appendActivitySummary(sb, result.activitySummary());
        appendFlaggedEntries(sb, result.flaggedEntries());
        appendSuspiciousIps(sb, result.suspiciousIps());
        appendUnknownPatterns(sb, result.unknownPatterns());
        appendMalformedLines(sb, malformedLines);

        return sb.toString();
    }

    private void appendActivitySummary(StringBuilder sb, Map<String, Integer> summary) {
        sb.append("--- Activity Summary ---\n");
        if (summary.isEmpty()) {
            sb.append("None found\n");
        } else {
            for (Map.Entry<String, Integer> entry : summary.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }
        sb.append("\n");
    }

    private void appendFlaggedEntries(StringBuilder sb, List<LogEntry> flaggedEntries) {
        sb.append("--- Flagged Entries ---\n");
        if (flaggedEntries.isEmpty()) {
            sb.append("None found\n");
        } else {
            for (LogEntry entry : flaggedEntries) {
                sb.append(formatEntryLine(entry)).append("\n");
            }
        }
        sb.append("\n");
    }

    private void appendSuspiciousIps(StringBuilder sb, List<SuspiciousIp> suspiciousIps) {
        sb.append("--- Suspicious Activity by IP ---\n");
        if (suspiciousIps.isEmpty()) {
            sb.append("None found\n");
        } else {
            for (SuspiciousIp ip : suspiciousIps) {
                sb.append(ip.ip()).append(": ").append(ip.entryCount()).append(" entries\n");
            }
        }
        sb.append("\n");
    }

    private void appendUnknownPatterns(StringBuilder sb, List<UnknownPattern> unknownPatterns) {
        sb.append("--- Unknown Patterns ---\n");
        if (unknownPatterns.isEmpty()) {
            sb.append("None found\n");
        } else {
            for (UnknownPattern pattern : unknownPatterns) {
                sb.append("Line ").append(pattern.lineNumber()).append(": ")
                        .append(pattern.rawContent()).append("\n");
            }
        }
        sb.append("\n");
    }

    private void appendMalformedLines(StringBuilder sb, List<MalformedLine> malformedLines) {
        sb.append("--- Malformed Lines ---\n");
        if (malformedLines.isEmpty()) {
            sb.append("None found\n");
        } else {
            for (MalformedLine line : malformedLines) {
                sb.append("Line ").append(line.lineNumber()).append(": ")
                        .append(line.rawContent()).append("\n");
            }
        }
    }

    private String formatEntryLine(LogEntry entry) {
        if (entry.originalLine() != null) {
            return entry.originalLine();
        }
        return entry.timestamp().format(TIMESTAMP_FORMAT) + " | " + entry.level() + " | "
                + entry.sourceIp() + " | " + entry.target() + " | " + entry.action();
    }
}
