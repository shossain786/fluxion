package com.fluxion.report;

public class Step {
    private String description;
    private String status;
    private String screenshotPath;

    public Step(String description, String status, String screenshotPath) {
        this.description = description;
        this.status = status;
        this.screenshotPath = screenshotPath;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }
}
