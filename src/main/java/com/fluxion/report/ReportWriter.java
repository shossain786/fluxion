package com.fluxion.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReportWriter {

    /**
     * Writes the HTML content to the specified file path.
     *
     * @param outputFilePath Path where the HTML report should be saved.
     * @param htmlContent    The HTML content to be written to the file.
     */
    public void writeReport(String outputFilePath, String htmlContent) {
        File reportFile = new File(outputFilePath);

        // Ensure the directory exists; if not, create it
        File parentDir = reportFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean dirsCreated = parentDir.mkdirs();
            if (!dirsCreated) {
                throw new RuntimeException("Failed to create directories for the report at: " + parentDir.getAbsolutePath());
            }
        }

        // Write the content to the file
        try (FileWriter writer = new FileWriter(reportFile)) {
            writer.write(htmlContent);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write the report to " + outputFilePath + ": " + e.getMessage(), e);
        }
    }
}
