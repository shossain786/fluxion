package com.fluxion.report;

import java.io.FileWriter;
import java.io.IOException;

public class ReportWriter {
    public void writeReport(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write report: " + e.getMessage(), e);
        }
    }
}
