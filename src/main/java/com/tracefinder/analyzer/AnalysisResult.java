package com.tracefinder.analyzer;

import com.tracefinder.model.LogEntry;
import com.tracefinder.model.UnknownPattern;

import java.util.List;
import java.util.Map;

public record AnalysisResult(
        Map<String, Integer> activitySummary,
        List<LogEntry> flaggedEntries,
        List<SuspiciousIp> suspiciousIps,
        List<UnknownPattern> unknownPatterns
) {}