package com.fluxion.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.util.UUID;

public class ScreenshotUtil {
    public static String captureScreenshot(WebDriver driver, String scenarioName) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String uniqueId = UUID.randomUUID().toString();
            String screenshotPath = "target/screenshots/" + scenarioName + "_" + uniqueId + ".png";
            Files.copy(screenshot.toPath(), new File(screenshotPath).toPath());
            return screenshotPath;
        } catch (Exception e) {
            throw new RuntimeException("Failed to capture screenshot: " + e.getMessage(), e);
        }
    }
}
