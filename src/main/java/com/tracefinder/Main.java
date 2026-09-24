package com.tracefinder;

public class Main {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java -jar tracefinder.jar <logFile> <rulebookFile> <outputFile>");
            System.exit(1);
        }

        String logFilePath = args[0];
        String rulebookFilePath = args[1];
        String reportFilePath = args[2];

        System.out.println("Log file: " + logFilePath);
        System.out.println("Rulebook: " + rulebookFilePath);
        System.out.println("Report will be written to: " + reportFilePath);
    }
}