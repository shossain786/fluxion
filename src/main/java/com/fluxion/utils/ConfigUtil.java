package com.fluxion.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class ConfigUtil {
    private static final Map<String, Object> config;

    static {
        try (InputStream input = ConfigUtil.class.getClassLoader().getResourceAsStream("config.yaml")) {
            if (input == null) {
                throw new RuntimeException("Configuration file 'config.yaml' not found in resources.");
            }
            Yaml yaml = new Yaml();
            config = yaml.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration from YAML file", e);
        }
    }

    public static String getHtmlReportPath() {
        Map<String, Object> htmlReport = (Map<String, Object>) config.get("htmlReport");
        if (htmlReport != null) {
            return (String) htmlReport.get("path");
        }
        throw new RuntimeException("HTML report path is not defined in the configuration file.");
    }
}
