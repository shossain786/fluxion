package com.fluxion.report;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportManager {
    private static final String REPORT_TEMPLATE = "src/main/resources/templates/report_template.html";
    private static final String REPORT_OUTPUT = "target/fluxion-report.html";

    // Thread-safe list to store all scenarios
    private static final List<Scenario> scenarios = Collections.synchronizedList(new ArrayList<>());

    // ThreadLocal for managing current scenario
    private static final ThreadLocal<Scenario> currentScenario = new ThreadLocal<>();

    private static String applicationName;
    private static String suiteType;

    public static void initializeReport(String appName, String suite) {
        applicationName = appName;
        suiteType = suite;
    }

    public static void startScenario(String scenarioName) {
        Scenario scenario = new Scenario(scenarioName);
        scenarios.add(scenario);
        currentScenario.set(scenario);
    }

    public static Scenario getCurrentScenario() {
        return currentScenario.get();
    }

    public static void endScenario(String status) {
        Scenario scenario = currentScenario.get();
        if (scenario != null) {
            scenario.setStatus(status);
        }
    }

    public static void generateReport() {
        try (FileWriter writer = new FileWriter(REPORT_OUTPUT)) {
            StringBuilder content = new StringBuilder();

            // Header
            content.append("<html><head><title>Fluxion Execution Report</title></head><body>");
            content.append("<h1>Execution Report</h1>");
            content.append("<p>Application: ").append(applicationName).append("</p>");
            content.append("<p>Suite Type: ").append(suiteType).append("</p>");
            content.append("<p>Total Scenarios: ").append(scenarios.size()).append("</p>");

            // Scenario Details
            for (Scenario scenario : scenarios) {
                content.append("<h2>").append(scenario.getName()).append(" - ")
                        .append(scenario.getStatus()).append("</h2>");
                content.append("<ul>");
                for (Step step : scenario.getSteps()) {
                    content.append("<li>").append(step.getDescription())
                            .append(" - ").append(step.getStatus());
                    if (step.getScreenshotPath() != null) {
                        content.append(" (<a href=\"").append(step.getScreenshotPath()).append("\">Screenshot</a>)");
                    }
                    content.append("</li>");
                }
                content.append("</ul>");
            }

            // Footer
            content.append("</body></html>");

            writer.write(content.toString());
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate report: " + e.getMessage(), e);
        }
    }
}
