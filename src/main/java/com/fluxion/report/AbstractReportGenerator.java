package com.fluxion.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public abstract class AbstractReportGenerator implements ReportGenerator {

    private final JsonNode reportData;

    public AbstractReportGenerator(String jsonFilePath) {
        this.reportData = parseJson(jsonFilePath);
    }

    protected JsonNode getReportData() {
        return this.reportData;
    }

    private JsonNode parseJson(String jsonFilePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = new File(jsonFilePath);
            if (!jsonFile.exists() || !jsonFile.isFile()) {
                throw new RuntimeException("The JSON file does not exist or is not a valid file: " + jsonFilePath);
            }
            return objectMapper.readTree(jsonFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Cucumber JSON report: " + jsonFilePath, e);
        }
    }

    /**
     * Formats a duration from nanoseconds to a human-readable string in seconds.
     *
     * @param durationInNanoSeconds The duration in nanoseconds.
     * @return The formatted duration as a string.
     */
    protected String formatDuration(long durationInNanoSeconds) {
        long seconds = durationInNanoSeconds / 1_000_000_000;
        return seconds + "s";
    }

    /**
     * Abstract method to enforce implementation in concrete classes.
     * Subclasses must handle how the report is generated and where it is saved.
     */
    @Override
    public abstract void generateReport();
}
