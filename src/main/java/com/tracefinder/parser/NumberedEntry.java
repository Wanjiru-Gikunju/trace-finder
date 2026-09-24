package com.tracefinder.parser;

import com.tracefinder.model.LogEntry;

public record NumberedEntry(int lineNumber, LogEntry entry) {}