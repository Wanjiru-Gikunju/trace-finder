package com.tracefinder.parser;

import com.tracefinder.model.LogEntry;
import com.tracefinder.model.MalformedLine;

import java.util.List;

public record ParsedLog(List<LogEntry> entries, List<MalformedLine> malformedLines) {}
