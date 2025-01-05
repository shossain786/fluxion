package com.fluxion.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class HtmlReportGenerator {

    private static final String DEFAULT_REPORT_PATH = "target/fluxion-reports/execution-report.html";
    private final JsonNode reportData;
    private final String reportPath;

    public HtmlReportGenerator(String jsonFilePath) {
        this.reportData = parseJson(jsonFilePath);
        this.reportPath = DEFAULT_REPORT_PATH;
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

    public void generateReport() {
        try {
            // Ensure the report directory exists
            Path outputPath = Path.of(reportPath).getParent();
            if (outputPath != null && !Files.exists(outputPath)) {
                Files.createDirectories(outputPath);
            }

            // Generate the HTML report
            try (FileWriter writer = new FileWriter(reportPath)) {
                AtomicInteger totalTests = new AtomicInteger();
                AtomicInteger passedTests = new AtomicInteger();
                AtomicInteger failedTests = new AtomicInteger();
                AtomicInteger totalSteps = new AtomicInteger();
                AtomicInteger failedSteps = new AtomicInteger();
                long totalDuration = 0;

                writer.write("<!DOCTYPE html>");
                writer.write("<html lang='en'>");
                writer.write("<head>");
                writer.write("<meta charset='UTF-8'>");
                writer.write("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                writer.write("<title>Fluxion Execution Report</title>");
                writer.write("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
                writer.write("<style>");
                writer.write("body { padding: 20px; } .table-hover tbody tr:hover { background-color: #f8f9fa; } .status-pass { color: green; font-weight: bold; } .status-fail { color: red; font-weight: bold; }");
                writer.write("</style>");
                writer.write("</head>");
                writer.write("<body>");

                writer.write("<div class='container'>");
                writer.write("<h1 class='text-center'>Test Execution Report</h1>");

                writer.write("<div class='mb-4'>");
                writer.write("<h2>Summary</h2>");
                writer.write("<table class='table table-bordered'>");
                writer.write("<tr><th>Total Scenarios</th><td id='total-tests'></td></tr>");
                writer.write("<tr><th>Passed Scenarios</th><td id='passed-tests'></td></tr>");
                writer.write("<tr><th>Failed Scenarios</th><td id='failed-tests'></td></tr>");
                writer.write("<tr><th>Total Steps</th><td id='total-steps'></td></tr>");
                writer.write("<tr><th>Failed Steps</th><td id='failed-steps'></td></tr>");
                writer.write("<tr><th>Total Duration</th><td id='total-duration'></td></tr>");
                writer.write("</table>");
                writer.write("</div>");

                writer.write("<div class='accordion' id='accordionExample'>");

                if (reportData != null) {
                    for (JsonNode feature : reportData) {
                        String featureName = feature.path("name").asText("Unknown Feature");
                        for (JsonNode element : feature.path("elements")) {
                            totalTests.incrementAndGet();
                            String scenarioName = element.path("name").asText("Unnamed Scenario");
                            String status = element.path("steps").path(element.path("steps").size() - 1).path("result").path("status").asText("unknown");

                            if ("passed".equalsIgnoreCase(status)) {
                                passedTests.incrementAndGet();
                            } else if ("failed".equalsIgnoreCase(status)) {
                                failedTests.incrementAndGet();
                            }

                            writer.write("<div class='accordion-item'>");
                            writer.write("<h2 class='accordion-header' id='heading-" + totalTests + "'>");
                            writer.write("<button class='accordion-button' type='button' data-bs-toggle='collapse' data-bs-target='#collapse-" + totalTests + "' aria-expanded='true' aria-controls='collapse-" + totalTests + "'>");
                            writer.write("Scenario: " + scenarioName + " (Status: <span class='" + ("passed".equalsIgnoreCase(status) ? "status-pass" : "status-fail") + "'>" + capitalize(status) + "</span>)");
                            writer.write("</button>");
                            writer.write("</h2>");
                            writer.write("<div id='collapse-" + totalTests + "' class='accordion-collapse collapse' aria-labelledby='heading-" + totalTests + "' data-bs-parent='#accordionExample'>");
                            writer.write("<div class='accordion-body'>");

                            writer.write("<table class='table table-bordered'>");
                            writer.write("<thead><tr><th>Step</th><th>Status</th><th>Duration</th><th>Screenshot</th></tr></thead>");
                            writer.write("<tbody>");

                            for (JsonNode step : element.path("steps")) {
                                totalSteps.incrementAndGet();
                                String stepName = step.path("name").asText("Unnamed Step");
                                String stepStatus = step.path("result").path("status").asText("unknown");
                                long stepDuration = step.path("result").path("duration").asLong(0);
                                String screenshotPath = step.path("screenshot").asText("");

                                if ("failed".equalsIgnoreCase(stepStatus)) {
                                    failedSteps.incrementAndGet();
                                }

                                writer.write("<tr>");
                                writer.write("<td>" + stepName + "</td>");
                                writer.write("<td class='" + ("passed".equalsIgnoreCase(stepStatus) ? "status-pass" : "status-fail") + "'>" + capitalize(stepStatus) + "</td>");
                                writer.write("<td>" + formatDuration(stepDuration) + "</td>");
                                if (!screenshotPath.isEmpty()) {
                                    writer.write("<td><img src='" + screenshotPath + "' alt='Screenshot' style='max-width: 100px;'></td>");
                                } else {
                                    writer.write("<td>N/A</td>");
                                }
                                writer.write("</tr>");
                            }

                            writer.write("</tbody>");
                            writer.write("</table>");
                            writer.write("</div>");
                            writer.write("</div>");
                            writer.write("</div>");
                        }
                    }
                }

                writer.write("</div>"); // End accordion
                writer.write("</div>"); // End container
                writer.write("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>");
                writer.write("<script>");
                writer.write("document.getElementById('total-tests').innerText = " + totalTests + ";");
                writer.write("document.getElementById('passed-tests').innerText = " + passedTests + ";");
                writer.write("document.getElementById('failed-tests').innerText = " + failedTests + ";");
                writer.write("document.getElementById('total-steps').innerText = " + totalSteps + ";");
                writer.write("document.getElementById('total-tests').innerText = " + totalTests.get() + ";");
                writer.write("document.getElementById('passed-tests').innerText = " + passedTests.get() + ";");
                writer.write("document.getElementById('failed-tests').innerText = " + failedTests.get() + ";");
                writer.write("document.getElementById('total-steps').innerText = " + totalSteps.get() + ";");
                writer.write("document.getElementById('failed-steps').innerText = " + failedSteps.get() + ";");
                writer.write("document.getElementById('total-duration').innerText = '" + formatDuration(totalDuration) + "';");
                writer.write("</script>");
                writer.write("</body>");
                writer.write("</html>");
            }

            System.out.println("HTML report generated at: " + reportPath);

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate HTML report: " + reportPath, e);
        }
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private String formatDuration(long durationInNano) {
        long milliseconds = durationInNano / 1_000_000;
        long seconds = milliseconds / 1000;
        milliseconds %= 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d.%03d", minutes, seconds, milliseconds);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the path to the JSON report file: ");
        String jsonFilePath = scanner.nextLine();

        HtmlReportGenerator generator = new HtmlReportGenerator(jsonFilePath);
        generator.generateReport();
    }
}
