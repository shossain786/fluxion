package com.fluxion.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public abstract class AbstractReportGenerator implements ReportGenerator {

    private JsonNode reportData;

    public AbstractReportGenerator(String jsonFilePath) {
        this.reportData = parseJson(jsonFilePath);
    }

    protected JsonNode getReportData() {
        return this.reportData;
    }

    private JsonNode parseJson(String jsonFilePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(new File(jsonFilePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Cucumber JSON report: " + jsonFilePath, e);
        }
    }

    protected String formatDuration(long durationInNanoSeconds) {
        long seconds = durationInNanoSeconds / 1_000_000_000;
        return seconds + "s";
    }

    // Abstract method to enforce implementation in concrete classes
    public abstract void generateReport(String outputFilePath);
}
