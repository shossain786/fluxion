package com.fluxion.utils;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.Map;

public class YamlReader {
    public static Map<String, Object> readYamlFile(String filePath) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = YamlReader.class.getClassLoader().getResourceAsStream(filePath)) {
            return yaml.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read YAML file: " + filePath, e);
        }
    }
}
