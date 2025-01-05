package com.fluxion.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class CucumberJsonParser {
    public static JsonNode parseJsonReport(String jsonFilePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(new File(jsonFilePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Cucumber JSON report: " + jsonFilePath, e);
        }
    }
}
