package com.fluxion.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.fluxion.utils.ConfigUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HtmlReportGenerator extends AbstractReportGenerator {

    private static final String DEFAULT_REPORT_PATH = "target/custom-reports/execution-report.html";
    private final String reportPath;

    /**
     * Constructor for HtmlReportGenerator.
     *
     * @param jsonFilePath Path to the Cucumber JSON report.
     */
    public HtmlReportGenerator(String jsonFilePath) {
        super(jsonFilePath);
        // Fetch the report path from configuration; fallback to the default path if not provided.
        this.reportPath = ConfigUtil.getProperty("html.report.path", DEFAULT_REPORT_PATH);
    }

    /**
     * Generates the HTML report based on the parsed JSON data.
     *
     * @param outputFilePath Not used in this implementation but kept for interface compatibility.
     */
    @Override
    public void generateReport(String outputFilePath) {
        try {
            // Ensure the report directory exists
            Path outputDir = Path.of(reportPath).getParent();
            if (outputDir != null && !Files.exists(outputDir)) {
                Files.createDirectories(outputDir);
            }

            // Generate the HTML report
            try (FileWriter writer = new FileWriter(reportPath)) {
                writer.write(generateHtmlContent());
            }

            System.out.println("HTML report generated successfully at: " + reportPath);

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate HTML report", e);
        }
    }

    /**
     * Generates the HTML content for the report.
     *
     * @return HTML content as a String.
     */
    private String generateHtmlContent() {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><head><title>Execution Report</title></head><body>");
        htmlBuilder.append("<h1>Test Execution Report</h1>");
        htmlBuilder.append("<table border='1'>");
        htmlBuilder.append("<tr><th>Feature</th><th>Scenario</th><th>Status</th><th>Duration</th></tr>");

        try {
            JsonNode reportData = getReportData();
            for (JsonNode feature : reportData) {
                String featureName = feature.get("name").asText();
                for (JsonNode element : feature.get("elements")) {
                    String scenarioName = element.get("name").asText();
                    String status = extractScenarioStatus(element);
                    long duration = extractScenarioDuration(element);

                    htmlBuilder.append("<tr>");
                    htmlBuilder.append("<td>").append(featureName).append("</td>");
                    htmlBuilder.append("<td>").append(scenarioName).append("</td>");
                    htmlBuilder.append("<td>").append(status).append("</td>");
                    htmlBuilder.append("<td>").append(formatDuration(duration)).append("</td>");
                    htmlBuilder.append("</tr>");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while parsing JSON report data: " + e.getMessage(), e);
        }

        htmlBuilder.append("</table>");
        htmlBuilder.append("</body></html>");
        return htmlBuilder.toString();
    }

    /**
     * Extracts the status of the scenario from the JSON node.
     *
     * @param scenarioNode The JSON node representing the scenario.
     * @return The status of the scenario (e.g., "passed", "failed").
     */
    private String extractScenarioStatus(JsonNode scenarioNode) {
        JsonNode steps = scenarioNode.get("steps");
        if (steps != null && steps.size() > 0) {
            JsonNode lastStep = steps.get(steps.size() - 1);
            return lastStep.get("result").get("status").asText();
        }
        return "unknown";
    }

    /**
     * Extracts the duration of the scenario from the JSON node.
     *
     * @param scenarioNode The JSON node representing the scenario.
     * @return The duration of the scenario in nanoseconds.
     */
    private long extractScenarioDuration(JsonNode scenarioNode) {
        JsonNode steps = scenarioNode.get("steps");
        if (steps != null && steps.size() > 0) {
            JsonNode lastStep = steps.get(steps.size() - 1);
            return lastStep.get("result").get("duration").asLong();
        }
        return 0;
    }
}
