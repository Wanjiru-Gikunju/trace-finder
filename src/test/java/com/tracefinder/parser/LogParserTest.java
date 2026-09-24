package com.tracefinder.parser;

import com.tracefinder.model.LogEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LogParserTest {

    @Test
    void parseCleanLineIntoLogEntry() {
        // Arrange
        LogParser parser = new LogParser();
        String line = "2024-03-15 02:14:08 | INFO | 192.168.1.45 | /login | success";

        // Act
        ParseResult result = parser.parseLine(line, 1);

        // Assert
        assertNotNull(result.getEntry());
        LogEntry entry = result.getEntry();
        assertEquals(LocalDateTime.of(2024, 3, 15, 2, 14, 8), entry.timestamp());
        assertEquals("INFO", entry.level());
        assertEquals("192.168.1.45", entry.sourceIp());
        assertEquals("/login", entry.target());
        assertEquals("success", entry.action());
    }

    @Test 
    void missingFieldLineIsRecordedAsMalformed() {
        //Arrange
        LogParser parser = new LogParser();
        String line = "2024-03-15 02:17:00 | WARN | 203.0.113.42 | /login";

        //Act
        ParseResult result = parser.parseLine(line, 23);

        //Assert
        assertNull(result.getEntry());
        assertNotNull(result.getMalformedLine());
        assertEquals(23, result.getMalformedLine().lineNumber());
        assertEquals(line, result.getMalformedLine().rawContent());
    }
}