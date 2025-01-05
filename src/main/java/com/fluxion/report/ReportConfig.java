package com.fluxion.report;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ReportConfig {
    private String browser;
    private String baseUrl;
    private int timeout;
    private Screenshot screenshot;
    private HtmlReport htmlReport;

    // No-argument constructor
    public ReportConfig() {}

    // Constructor to load from YAML file
    public ReportConfig(String configFilePath) {
        try {
            // Create YAML ObjectMapper
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            // Read YAML file and deserialize to ReportConfig
            ReportConfig config = mapper.readValue(new File(configFilePath), ReportConfig.class);
            this.browser = config.browser;
            this.baseUrl = config.baseUrl;
            this.timeout = config.timeout;
            this.screenshot = config.screenshot;
            this.htmlReport = config.htmlReport;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read config file", e);
        }
    }

    // Getters and Setters for the fields
    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Screenshot getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(Screenshot screenshot) {
        this.screenshot = screenshot;
    }

    public HtmlReport getHtmlReport() {
        return htmlReport;
    }

    public void setHtmlReport(HtmlReport htmlReport) {
        this.htmlReport = htmlReport;
    }

    // Inner classes for complex fields
    public static class Screenshot {
        private String dir;

        // Getter and Setter
        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }
    }

    public static class HtmlReport {
        private String suiteName;
        private String outputFilePath;

        // Getter and Setter
        public String getSuiteName() {
            return suiteName;
        }

        public void setSuiteName(String suiteName) {
            this.suiteName = suiteName;
        }

        public String getOutputFilePath() {
            return outputFilePath;
        }

        public void setOutputFilePath(String outputFilePath) {
            this.outputFilePath = outputFilePath;
        }
    }
}
