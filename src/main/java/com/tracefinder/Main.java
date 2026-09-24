package com.tracefinder;

import com.tracefinder.analyzer.AnalysisResult;
import com.tracefinder.analyzer.Analyzer;
import com.tracefinder.model.LogEntry;
import com.tracefinder.model.MalformedLine;
import com.tracefinder.model.UnknownPattern;
import com.tracefinder.parser.LogFileProcessor;
import com.tracefinder.parser.LogFileReader;
import com.tracefinder.parser.NumberedEntry;
import com.tracefinder.parser.ParsedLog;
import com.tracefinder.report.ReportFormatter;
import com.tracefinder.report.ReportWriter;
import com.tracefinder.rulebook.MatchResult;
import com.tracefinder.rulebook.RulebookLoader;
import com.tracefinder.rulebook.RulebookMatcher;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java -jar tracefinder.jar <logFile> <rulebookFile> <outputFile>");
            System.exit(1);
        }

        String logFilePath = args[0];
        String rulebookFilePath = args[1];
        String reportFilePath = args[2];

        Map<String, Integer> rulebook = null;
        try {
            RulebookLoader rulebookLoader = new RulebookLoader();
            rulebook = rulebookLoader.loadRulebook(rulebookFilePath);
        } catch (IOException e) {
            System.err.println("Failed to read rulebook file '" + rulebookFilePath + "': " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Rulebook file '" + rulebookFilePath + "' is not valid CSV: " + e.getMessage());
            System.exit(1);
        }

        List<String> rawLines = null;
        try {
            LogFileReader logFileReader = new LogFileReader();
            rawLines = logFileReader.readLines(logFilePath);
        } catch (IOException e) {
            System.err.println("Failed to read log file '" + logFilePath + "': " + e.getMessage());
            System.exit(1);
        }

        LogFileProcessor processor = new LogFileProcessor();
        ParsedLog parsedLog = processor.parseLines(rawLines);

        RulebookMatcher matcher = new RulebookMatcher();
        Map<LogEntry, Integer> scoredEntries = new HashMap<>();
        List<UnknownPattern> unknownPatterns = new ArrayList<>();
        List<LogEntry> knownEntries = new ArrayList<>();

        for (NumberedEntry numberedEntry : parsedLog.entries()) {
            MatchResult matchResult = matcher.match(numberedEntry.entry(), rulebook, numberedEntry.lineNumber());

            if (matchResult.getScoredEntry() != null) {
                scoredEntries.put(matchResult.getScoredEntry(), matchResult.getSeverityScore());
                knownEntries.add(matchResult.getScoredEntry());
            } else {
                unknownPatterns.add(matchResult.getUnknownPattern());
            }
        }

        Analyzer analyzer = new Analyzer();
        AnalysisResult analysisResult = analyzer.analyze(knownEntries, scoredEntries, unknownPatterns);

        ReportFormatter formatter = new ReportFormatter();
        String reportText = formatter.format(analysisResult, parsedLog.malformedLines(), LocalDateTime.now());

        try {
            ReportWriter writer = new ReportWriter();
            writer.writeReport(reportText, reportFilePath);
        } catch (IOException e) {
            System.err.println("Failed to write report to '" + reportFilePath + "': " + e.getMessage());
            System.exit(1);
        }

        System.out.println("Report written to " + reportFilePath);
    }
}