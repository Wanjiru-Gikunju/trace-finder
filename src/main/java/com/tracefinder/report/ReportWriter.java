package com.tracefinder.report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReportWriter {

    public void writeReport(String content, String filePath) throws IOException {
        Path path = Path.of(filePath);
        Files.writeString(path, content);
    }
}