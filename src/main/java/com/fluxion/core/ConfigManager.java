package com.fluxion.core;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class ConfigManager {
    private static Map<String, Object> config;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try {
            // Search for the config.yml file in the user's project repository
            String configPath = "src/test/resources/config/config.yml";
            InputStream inputStream = Files.newInputStream(Paths.get(configPath));
            Yaml yaml = new Yaml();
            config = yaml.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration from " + "src/test/resources/config/config.yml", e);
        }
    }

    public static String getConfig(String key) {
        return config.getOrDefault(key, "").toString();
    }
}
