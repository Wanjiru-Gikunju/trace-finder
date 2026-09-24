package com.tracefinder.rulebook;

import com.tracefinder.model.LogEntry;
import com.tracefinder.model.UnknownPattern;

import java.util.Map;

public class RulebookMatcher {

    public MatchResult match(LogEntry entry, Map<String, Integer> rulebook, int lineNumber) {
        if (rulebook.containsKey(entry.level())) {
            int score = rulebook.get(entry.level());
            return MatchResult.matched(entry, score);
        } else {
            UnknownPattern unknown = new UnknownPattern(lineNumber, formatEntry(entry));
            return MatchResult.unknown(unknown);
        }
    }

    private String formatEntry(LogEntry entry) {
        return entry.timestamp() + " | " + entry.level() + " | " + entry.sourceIp()
                + " | " + entry.target() + " | " + entry.action();
    }
}