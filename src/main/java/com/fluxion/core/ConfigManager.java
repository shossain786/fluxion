package com.fluxion.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.util.Map;

public class ConfigManager {
    private static Map<String, Object> config;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            File configFile = new File("src/test/resources/config/config.yml");
            config = mapper.readValue(configFile, Map.class);
        } catch (Exception e) {
            System.err.println("Failed to load configuration file: src/test/resources/config/config.yml");
            e.printStackTrace();
            // Use default values if file loading fails
            config = Map.of(
                    "screenshot.dir", "target/screenshots",
                    "htmlReport.path", "target/custom-reports/execution-report.html",
                    "browser", "chrome",
                    "baseUrl", "https://google.com",
                    "timeout", 30
            );
        }
    }

    public static String getConfig(String key) {
        Object value = config.get(key);
        return value != null ? value.toString() : null;
    }

    public static String getNestedConfig(String parentKey, String childKey) {
        Object parent = config.get(parentKey);
        if (parent instanceof Map) {
            Object childValue = ((Map<?, ?>) parent).get(childKey);
            return childValue != null ? childValue.toString() : null;
        }
        return null;
    }
}
