package com.fluxion.report;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HtmlReportGenerator {
    // Create a logger instance for this class
    private static final Logger logger = LoggerFactory.getLogger(HtmlReportGenerator.class);

    private final ReportConfig config;
    private final ChartGenerator chartGenerator;
    private final HtmlReportBuilder builder;
    private final ReportWriter writer;

    public HtmlReportGenerator() {
        // Hard-coded path to the config file
        String configFilePath = "src/test/resources/config/config.yml";  // Replace with your actual path

        this.config = new ReportConfig(configFilePath); // Load from YAML
        this.chartGenerator = new ChartGenerator();
        this.builder = new HtmlReportBuilder(config.getHtmlReport().getSuiteName(), config.getHtmlReport().getSuiteName());
        this.writer = new ReportWriter();
    }


    public void generateReportFromCucumberJson(String cucumberJsonFilePath) {
        try {
            logger.info("Generating report from Cucumber JSON: {}", cucumberJsonFilePath);
            List<Map<String, Object>> features = parseCucumberJson(cucumberJsonFilePath);
            List<Map<String, String>> scenarios = extractScenarioDetails(features);
            String outputFilePath = config.getHtmlReport().getOutputFilePath();
            generateReport(scenarios, outputFilePath);
        } catch (IOException e) {
            logger.error("Failed to generate report from Cucumber JSON file: {}", cucumberJsonFilePath, e);
            throw new RuntimeException("Failed to generate report", e);
        }
    }

    private List<Map<String, Object>> parseCucumberJson(String cucumberJsonFilePath) throws IOException {
        logger.debug("Parsing Cucumber JSON file: {}", cucumberJsonFilePath);
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> features = mapper.readValue(new File(cucumberJsonFilePath), new TypeReference<List<Map<String, Object>>>() {});
        logger.debug("Successfully parsed Cucumber JSON file: {}", cucumberJsonFilePath);
        return features;
    }

    public void generateReport(List<Map<String, String>> scenarios, String outputFilePath) {
        logger.info("Generating HTML report for {} scenarios", scenarios.size());
        String chartData = chartGenerator.generateChartData(scenarios);
        String htmlContent = builder.buildHtml(scenarios, chartData);
        writer.writeReport(outputFilePath, htmlContent);
        logger.info("Report generated successfully at: {}", outputFilePath);
    }

    private List<Map<String, String>> extractScenarioDetails(List<Map<String, Object>> features) {
        logger.debug("Extracting scenario details from features");
        return features.stream()
                .flatMap(feature -> {
                    List<Map<String, Object>> elements = (List<Map<String, Object>>) feature.get("elements");
                    return elements.stream().map(element -> {
                        String name = (String) element.get("name");
                        List<Map<String, Object>> steps = (List<Map<String, Object>>) element.get("steps");
                        String status = determineScenarioStatus(steps);
                        return Map.of("name", name, "status", status);
                    });
                })
                .toList();
    }

    private String determineScenarioStatus(List<Map<String, Object>> steps) {
        logger.debug("Determining scenario status");
        if (steps == null || steps.isEmpty()) {
            return "unknown";
        }
        return steps.stream()
                .map(step -> {
                    Map<String, Object> result = (Map<String, Object>) step.get("result");
                    return result != null ? (String) result.get("status") : "unknown";
                })
                .anyMatch(status -> !"passed".equals(status)) ? "failed" : "passed";
    }
}
