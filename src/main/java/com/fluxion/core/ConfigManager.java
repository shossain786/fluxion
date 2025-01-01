package com.fluxion.core;

import com.fluxion.utils.YamlReader;
import java.util.Map;

public class ConfigManager {
    private static Map<String, Object> config;

    static {
        config = YamlReader.readYamlFile("src/test/resources/config/config.yml");
    }

    public static String getConfig(String key) {
        return config.get(key).toString();
    }
}
