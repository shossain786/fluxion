package com.fluxion.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * LocatorLoader is a utility class responsible for loading and caching locators from YAML files.
 * Locators are loaded once per session and stored in a synchronized map for thread safety.
 */
public class LocatorLoader {

    // Thread-safe map for storing locators
    private static final Map<String, Map<String, String>> locators = Collections.synchronizedMap(new HashMap<>());

    // Private constructor to prevent instantiation
    private LocatorLoader() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Loads all YAML locator files from a specified directory into memory.
     *
     * @param locatorDirPath Path to the directory containing YAML locator files.
     * @throws RuntimeException if the directory does not exist, is not a directory, or locators cannot be loaded.
     */
    public static void loadLocators(String locatorDirPath) {
        try {
            Path locatorDir = Paths.get(locatorDirPath);

            if (!Files.exists(locatorDir) || !Files.isDirectory(locatorDir)) {
                throw new RuntimeException("Locator directory not found or is not a directory: " + locatorDirPath);
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
     * Loads a single YAML file and stores its locators in memory.
     *
     * @param filePath Path to the YAML locator file.
     * @throws RuntimeException if the file cannot be read or its contents are invalid.
     */
    private static void loadLocatorFile(Path filePath) {
        try (FileInputStream inputStream = new FileInputStream(filePath.toFile())) {
            Yaml yaml = new Yaml();
            Object data = yaml.load(inputStream);

            if (!(data instanceof Map)) {
                throw new IllegalArgumentException("Invalid YAML structure in file: " + filePath);
            }

            @SuppressWarnings("unchecked")
            Map<String, String> locatorData = (Map<String, String>) data;

            String fileName = filePath.getFileName().toString();
            String locatorName = fileName.substring(0, fileName.lastIndexOf('.'));

            locators.put(locatorName, locatorData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load locator file: " + filePath, e);
        }
    }

    /**
     * Retrieves the locators for a specific locator group (file).
     *
     * @param locatorName Name of the locator file (without extension).
     * @return A map of locators, where the key is the element's name, and the value is its locator.
     * @throws RuntimeException if the requested locator group is not found.
     */
    public static Map<String, String> getLocatorGroup(String locatorName) {
        synchronized (locators) {
            if (!locators.containsKey(locatorName)) {
                throw new RuntimeException("Locator group not found: " + locatorName);
            }
            return locators.get(locatorName);
        }
    }

    /**
     * Retrieves a specific locator's value by locator group and field name.
     *
     * @param locatorGroup Name of the locator group (file).
     * @param fieldName    The name of the field whose locator is needed.
     * @return The locator value as a string.
     * @throws RuntimeException if the locator group or field name is not found.
     */
    public static String getLocator(String locatorGroup, String fieldName) {
        synchronized (locators) {
            if (!locators.containsKey(locatorGroup)) {
                throw new RuntimeException("Locator group not found: " + locatorGroup);
            }
            Map<String, String> groupLocators = locators.get(locatorGroup);
            if (!groupLocators.containsKey(fieldName)) {
                throw new RuntimeException("Field name not found in locator group: " + fieldName);
            }
            return groupLocators.get(fieldName);
        }
    }

    /**
     * Clears all cached locators. Useful for tests or reloading locators at runtime.
     */
    public static void clearLocators() {
        synchronized (locators) {
            locators.clear();
        }
    }
}
