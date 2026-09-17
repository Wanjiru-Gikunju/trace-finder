package com.tracefinder.parser;

import com.tracefinder.model.LogEntry;
import com.tracefinder.model.MalformedLine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class LogParser {
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Optional<LogEntry> parseLine(String rawLine) {
        if(rawLine == null || rawLine.isBlank()) {
            return Optional.empty();
        }

        String[] fields = rawLine.split("\\|"); 

        if(fields.length != 5) {
            return  Optional.empty();
        }

        String timestampText = fields[0].trim();
        String level = fields[1].trim();
        String sourceIp = fields[2].trim();
        String target = fields[3].trim();
        String action = fields[4].trim();

        LocalDateTime timestamp;
        try {
            timestamp = LocalDateTime.parse(timestampText, TIMESTAMP_FORMAT);
        }catch (DateTimeParseException e) {
            return Optional.empty();
        }

        LogEntry entry = new LogEntry(timestamp,level,sourceIp, target, action);
        return Optional.of(entry);
    }
    
}
