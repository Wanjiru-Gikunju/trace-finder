package com.tracefinder.parser;

import com.tracefinder.model.MalformedLine;

import java.util.ArrayList;
import java.util.List;

public class LogFileProcessor {
     private final LogParser parser = new LogParser();

    public ParsedLog parseLines(List<String> rawLines) {
        List<NumberedEntry> entries = new ArrayList<>();
        List<MalformedLine> malformedLines = new ArrayList<>();

        for (int i = 0; i < rawLines.size(); i++) {
            String line = rawLines.get(i);
            int lineNumber = i + 1;

            ParseResult result = parser.parseLine(line, lineNumber);

            if (result.getEntry() != null) {
                entries.add(new NumberedEntry(lineNumber, result.getEntry()));
            } else {
                malformedLines.add(result.getMalformedLine());
            }
        }

        return new ParsedLog(entries, malformedLines);

    }
}
