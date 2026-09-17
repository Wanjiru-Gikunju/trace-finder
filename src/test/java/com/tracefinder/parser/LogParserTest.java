package com.tracefinder.parser;

import com.tracefinder.model.LogEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LogParserTest {

    @Test 
    void parseCleanLineIntoLogEntry() {
        //Arrange
        LogParser parser = new LogParser();
        String line = "2024-03-15 02:14:08 | INFO | 192.168.1.45 | /login | success";

        //Act
        Optional<LogEntry> result = parser.parseLine(line);

        //Assert
        assertTrue(result.isPresent());
        LogEntry entry = result.get();
        assertEquals(LocalDateTime.of(2024, 3, 15, 2, 14, 8), entry.timestamp());
        assertEquals("INFO", entry.level());
        assertEquals("192.168.1.45", entry.sourceIp());
        assertEquals("/login", entry.target());
        assertEquals("success", entry.action());
    }
    
}
