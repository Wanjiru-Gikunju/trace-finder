package com.tracefinder.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogFileProcessorTest {

    @Test
    void separatesCleanAndMalformedLines() {
        // Arrange
        LogFileProcessor processor = new LogFileProcessor();
        List<String> lines = List.of(
                "2024-03-15 02:14:08 | INFO | 192.168.1.45 | /login | success",
                "2024-03-15 02:17:00 | WARN | 203.0.113.42 | /login"
        );

        // Act
        ParsedLog result = processor.parseLines(lines);

        // Assert
        assertEquals(1, result.entries().size());
        assertEquals(1, result.malformedLines().size());
        assertEquals(2, result.malformedLines().get(0).lineNumber());
    }
}