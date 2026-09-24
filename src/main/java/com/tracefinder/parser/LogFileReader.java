package com.tracefinder.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LogFileReader {

    public List<String> readLines(String filePath) throws IOException {
        Path path = Path.of(filePath);
        return Files.readAllLines(path);
    }
    
}
