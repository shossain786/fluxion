package com.fluxion.report;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.FileWriter;
import java.io.IOException;

public class CustomReportGenerator {

    public static void generateHtmlReport(JsonNode reportData, String outputFilePath) {
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            writer.write("<html><head><title>Execution Report</title></head><body>");
            writer.write("<h1>Test Execution Report</h1>");
            writer.write("<table border='1'>");
            writer.write("<tr><th>Feature</th><th>Scenario</th><th>Status</th><th>Duration</th></tr>");

            for (JsonNode feature : reportData) {
                String featureName = feature.get("name").asText();
                for (JsonNode element : feature.get("elements")) {
                    String scenarioName = element.get("name").asText();
                    String status = element.get("steps")
                            .get(element.get("steps").size() - 1)
                            .get("result").get("status").asText();
                    String duration = element.get("steps")
                            .get(element.get("steps").size() - 1)
                            .get("result").get("duration").asText();

                    writer.write("<tr>");
                    writer.write("<td>" + featureName + "</td>");
                    writer.write("<td>" + scenarioName + "</td>");
                    writer.write("<td>" + status + "</td>");
                    writer.write("<td>" + duration + "</td>");
                    writer.write("</tr>");
                }
            }

            writer.write("</table>");
            writer.write("</body></html>");
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate HTML report", e);
        }
    }
}
