package com.fluxion.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * LocatorLoader is a utility class that handles loading and caching locators from YAML files.
 * Locators are loaded only once during the execution and made available throughout the session.
 */
public class LocatorLoader {

    private static final Map<String, Map<String, String>> locators = new HashMap<>();

    // Private constructor to prevent instantiation
    private LocatorLoader() {}

    /**
     * Loads all locator files from a given directory.
     *
     * @param locatorDirPath Path to the directory containing YAML locator files.
     */
    public static void loadLocators(String locatorDirPath) {
        try {
            Path locatorDir = Paths.get(locatorDirPath);

            if (!Files.exists(locatorDir) || !Files.isDirectory(locatorDir)) {
                throw new RuntimeException("Locator directory not found: " + locatorDirPath);
            }

            Files.walk(locatorDir)
                    .filter(Files::isRegularFile)
                    .filter(file -> file.toString().endsWith(".yml"))
                    .forEach(LocatorLoader::loadLocatorFile);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load locators from directory: " + locatorDirPath, e);
        }
    }

    /**
     * Loads a single YAML locator file and stores its data in memory.
     *
     * @param filePath Path to the YAML file.
     */
    private static void loadLocatorFile(Path filePath) {
        try (FileInputStream inputStream = new FileInputStream(filePath.toFile())) {
            Yaml yaml = new Yaml();
            Map<String, String> locatorData = yaml.load(inputStream);

            String fileName = filePath.getFileName().toString();
            String locatorName = fileName.substring(0, fileName.lastIndexOf('.'));

            locators.put(locatorName, locatorData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load locator file: " + filePath, e);
        }
    }

    /**
     * Retrieves locator data for a specific locator name.
     *
     * @param locatorName Name of the locator file (without extension).
     * @return A map containing locator data (type and value).
     */
    public static Map<String, String> getLocator(String locatorName) {
        if (!locators.containsKey(locatorName)) {
            throw new RuntimeException("Locator not found: " + locatorName);
        }
        return locators.get(locatorName);
    }
}
