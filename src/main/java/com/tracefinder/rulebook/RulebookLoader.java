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

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] fields = line.split(",");
            String level = fields[0].trim();
            int score = Integer.parseInt(fields[1].trim());

            rulebook.put(level, score);
        }

        return rulebook;
    }
}