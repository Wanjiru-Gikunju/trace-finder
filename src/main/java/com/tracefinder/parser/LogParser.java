package com.tracefinder.parser;

import com.tracefinder.model.LogEntry;
import com.tracefinder.model.MalformedLine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LogParser {
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")
            .withResolverStyle(java.time.format.ResolverStyle.STRICT);

    public ParseResult parseLine(String rawLine, int lineNumber) {
        if (rawLine == null || rawLine.isBlank()) {
            MalformedLine malformed = new MalformedLine(lineNumber, rawLine == null ? "" : rawLine);
            return new ParseResult(null, malformed);
        }

        String[] fields = rawLine.split("\\|");

        if (fields.length != 5) {
            MalformedLine malformed = new MalformedLine(lineNumber, rawLine);
            return new ParseResult(null, malformed);
        }

        String timestampText = fields[0].trim();
        String level = fields[1].trim();
        String sourceIp = fields[2].trim();
        String target = fields[3].trim();
        String action = fields[4].trim();

        LocalDateTime timestamp;
        try {
            timestamp = LocalDateTime.parse(timestampText, TIMESTAMP_FORMAT);
        } catch (DateTimeParseException e) {
            MalformedLine malformed = new MalformedLine(lineNumber, rawLine);
            return new ParseResult(null, malformed);
        }

        LogEntry entry = new LogEntry(timestamp, level, sourceIp, target, action, rawLine);
        return new ParseResult(entry, null);
    }
}
