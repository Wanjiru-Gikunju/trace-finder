package com.tracefinder.parser;

import com.tracefinder.model.MalformedLine;

import java.util.List;

public record ParsedLog(List<NumberedEntry> entries, List<MalformedLine> malformedLines) {}
