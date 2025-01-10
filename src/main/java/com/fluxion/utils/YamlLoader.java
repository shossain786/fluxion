package com.fluxion.utils;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

public class YamlLoader {
    private static Map<String, Object> yamlData;

    static {
        try (InputStream inputStream = YamlLoader.class.getResourceAsStream("/locators.yml")) {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            yamlData = mapper.readValue(inputStream, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load YAML file", e);
        }
    }

    public static String getLocator(String section, String fieldName) {
        return (String) ((Map<String, Object>) yamlData.get(section)).get(fieldName);
    }
}
