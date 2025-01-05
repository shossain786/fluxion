package com.fluxion.utils;

import com.fluxion.core.ConfigManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ScreenshotUtil {

    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String dirPath = ConfigManager.getNestedConfig("screenshot", "dir");
            String targetPath = dirPath + "/" + screenshotName + "_" + java.util.UUID.randomUUID() + ".png";

            File targetFile = new File(targetPath);
            targetFile.getParentFile().mkdirs();
            Files.move(screenshot.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return targetPath;
        } catch (Exception e) {
            throw new RuntimeException("Failed to capture screenshot: " + e.getMessage(), e);
        }
    }
}
