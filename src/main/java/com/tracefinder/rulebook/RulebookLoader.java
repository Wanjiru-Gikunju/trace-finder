package com.tracefinder.rulebook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RulebookLoader {

    public Map<String, Integer> loadRulebook(String filePath) throws IOException {
        Path path = Path.of(filePath);
        List<String> lines = Files.readAllLines(path);
        return parseRulebookLines(lines);
    }

    Map<String, Integer> parseRulebookLines(List<String> lines) {
        Map<String, Integer> rulebook = new HashMap<>();

        if (lines.isEmpty() || !lines.get(0).trim().equals("level,severity_score")) {
            throw new IllegalArgumentException("Expected CSV header: level,severity_score");
        }

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] fields = line.split(",", -1);
            if (fields.length != 2 || fields[0].trim().isEmpty() || fields[1].trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid rulebook row " + (i + 1) + ": " + line);
            }
            String level = fields[0].trim();
            int score = Integer.parseInt(fields[1].trim());

            if (rulebook.putIfAbsent(level, score) != null) {
                throw new IllegalArgumentException("Duplicate rulebook level on row " + (i + 1) + ": " + level);
            }
        }

        return rulebook;
    }
}
