package com.tracefinder.parser;

import com.tracefinder.model.LogEntry;
import com.tracefinder.model.MalformedLine;

public class ParseResult {

    private final LogEntry entry;
    private final MalformedLine malformedLine;

    public ParseResult(LogEntry entry, MalformedLine malformedLine) {
        this.entry = entry;
        this.malformedLine = malformedLine;
    }
    
    public LogEntry getEntry() {
        return entry;
    }

    public MalformedLine getMalformedLine() {
        return malformedLine;
    }
}
