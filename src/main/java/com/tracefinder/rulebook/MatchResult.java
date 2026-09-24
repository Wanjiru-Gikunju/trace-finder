package com.tracefinder.rulebook;

import com.tracefinder.model.LogEntry;
import com.tracefinder.model.UnknownPattern;

public class MatchResult {

    private final LogEntry scoredEntry;
    private final int severityScore;
    private final UnknownPattern unknownPattern;

    private MatchResult(LogEntry scoredEntry, int severityScore, UnknownPattern unknownPattern) {
        this.scoredEntry = scoredEntry;
        this.severityScore = severityScore;
        this.unknownPattern = unknownPattern;
    }

    public static MatchResult matched(LogEntry entry, int severityScore) {
        return new MatchResult(entry, severityScore, null);
    }

    public static MatchResult unknown(UnknownPattern unknownPattern) {
        return new MatchResult(null, 0, unknownPattern);
    }

    public LogEntry getScoredEntry() {
        return scoredEntry;
    }

    public int getSeverityScore() {
        return severityScore;
    }

    public UnknownPattern getUnknownPattern() {
        return unknownPattern;
    }
}