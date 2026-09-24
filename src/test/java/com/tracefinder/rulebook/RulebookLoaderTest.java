package com.tracefinder.rulebook;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RulebookLoaderTest {

    @Test
    void parsesRulebookLinesIntoScoreMap() {
        // Arrange
        RulebookLoader loader = new RulebookLoader();
        List<String> lines = List.of(
                "level,severity_score",
                "INFO,1",
                "WARN,3",
                "ERROR,5",
                "ALERT,9"
        );

        // Act
        Map<String, Integer> rulebook = loader.parseRulebookLines(lines);

        // Assert
        assertEquals(4, rulebook.size());
        assertEquals(1, rulebook.get("INFO"));
        assertEquals(3, rulebook.get("WARN"));
        assertEquals(5, rulebook.get("ERROR"));
        assertEquals(9, rulebook.get("ALERT"));
    }
}