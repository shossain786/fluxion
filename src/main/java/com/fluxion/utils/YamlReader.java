package com.fluxion.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * YamlReader is a utility class for reading configuration data from YAML files.
 */
public class YamlReader {

    private static Map<String, Object> config;

    /**
     * Loads configuration from a YAML file into memory.
     *
     * @param filePath Path to the YAML file.
     */
    public static void loadYamlFile(String filePath) {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            Yaml yaml = new Yaml();
            config = yaml.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read YAML file: " + filePath, e);
        }
    }

    /**
     * Retrieves a property value from the loaded configuration.
     *
     * @param key Key of the property.
     * @return The value of the property.
     */
    public static Object getProperty(String key) {
        if (config == null) {
            throw new IllegalStateException("YAML file not loaded. Call loadYamlFile() first.");
        }
        return config.get(key);
    }

    /**
     * Retrieves locator directory paths from the configuration.
     *
     * @return A map containing locator directory paths.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getLocatorDirectories() {
        if (config == null) {
            throw new IllegalStateException("YAML file not loaded. Call loadYamlFile() first.");
        }
        return (Map<String, String>) config.get("locatorDir");
    }
}
