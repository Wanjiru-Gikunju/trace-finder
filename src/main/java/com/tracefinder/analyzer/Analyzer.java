package com.tracefinder.analyzer;

import com.tracefinder.model.LogEntry;
import com.tracefinder.model.UnknownPattern;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Comparator;

public class Analyzer {

    public Map<String, Integer> buildActivitySummary(List<LogEntry> knownEntries) {
        Map<String, Integer> summary = new HashMap<>();

        for (LogEntry entry : knownEntries) {
            String level = entry.level();
            int currentCount = summary.getOrDefault(level, 0);
            summary.put(level, currentCount + 1);
        }

        return summary;
    }
    public List<LogEntry> buildFlaggedEntries(Map<LogEntry, Integer> scoredEntries) {
    List<LogEntry> flagged = new ArrayList<>();

    for (Map.Entry<LogEntry, Integer> pair : scoredEntries.entrySet()) {
        if (pair.getValue() >= 3) {
            flagged.add(pair.getKey());
        }
    }

    flagged.sort(Comparator.comparingInt(entry -> -scoredEntries.get(entry)));

    return flagged;
  
   }
    public List<SuspiciousIp> buildSuspiciousIps(List<LogEntry> allEntries, Map<LogEntry, Integer> scoredEntries) {
        Map<String, List<LogEntry>> entriesByIp = new HashMap<>();

        for (LogEntry entry : allEntries) {
        String ip = entry.sourceIp();
        entriesByIp.computeIfAbsent(ip, key -> new ArrayList<>()).add(entry);
    }

    List<SuspiciousIp> suspiciousIps = new ArrayList<>();

    for (Map.Entry<String, List<LogEntry>> ipGroup : entriesByIp.entrySet()) {
        String ip = ipGroup.getKey();
        List<LogEntry> entriesForIp = ipGroup.getValue();

        boolean hasFlaggedEntry = false;
        for (LogEntry entry : entriesForIp) {
            Integer score = scoredEntries.get(entry);
            if (score != null && score >= 3) {
                hasFlaggedEntry = true;
                break;
            }
        }

        if (hasFlaggedEntry) {
            suspiciousIps.add(new SuspiciousIp(ip, entriesForIp.size()));
        }
    }

    suspiciousIps.sort(Comparator.comparingLong(SuspiciousIp::entryCount).reversed());

    return suspiciousIps;
  }

    public List<UnknownPattern> buildUnknownPatterns(List<UnknownPattern> unknownPatterns) {
        return new ArrayList<>(unknownPatterns);
    }

    public AnalysisResult analyze(List<LogEntry> knownEntries, List<LogEntry> allEntries,
                                  Map<LogEntry, Integer> scoredEntries, List<UnknownPattern> unknownPatterns) {
        Map<String, Integer> summary = buildActivitySummary(knownEntries);
        List<LogEntry> flagged = buildFlaggedEntries(scoredEntries);
        List<SuspiciousIp> suspicious = buildSuspiciousIps(allEntries, scoredEntries);
        List<UnknownPattern> unknown = buildUnknownPatterns(unknownPatterns);

        return new AnalysisResult(summary, flagged, suspicious, unknown);
    }

}
