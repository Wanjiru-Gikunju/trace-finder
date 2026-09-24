package com.tracefinder.analyzer;

import com.tracefinder.model.LogEntry;

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
   
}