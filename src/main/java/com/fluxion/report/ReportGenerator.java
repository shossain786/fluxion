package com.fluxion.report;

import com.fasterxml.jackson.databind.JsonNode;

public interface ReportGenerator {
    void generateReport(String outputFilePath);
}
