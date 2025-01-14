package com.fluxion.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Map;

public class LocatorManager {
    static Logger logger = LogManager.getLogger(LocatorManager.class);
    private static Map<String, Object> locators;
    private static boolean locatorsLoaded = false; // Flag to ensure locators are loaded only once

    // Static block for loading locators only once
    static {
        logger.debug("Loading the locators!");
        loadLocators();
    }

    // Load locators from the YAML file only once
    private static void loadLocators() {
        if (!locatorsLoaded) { // Check if locators are already loaded
            try {
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                File locatorFile = new File("src/test/resources/locators/mylocators.yml"); // Update file path if necessary
                locators = mapper.readValue(locatorFile, Map.class);
                locatorsLoaded = true; // Mark locators as loaded
            } catch (Exception e) {
                System.err.println("Failed to load locator file: src/test/resources/locators/mylocators.yml");
                e.printStackTrace();
                // Fallback to default locators if file loading fails
                locators = Map.of(
                        "loginButton", "//button[@id='login']",
                        "usernameField", "//input[@name='username']",
                        "passwordField", "//input[@name='password']"
                );
            }
        }
    }

    // Retrieve locator by key
    public static String getLocator(String key) {
        key = key.toLowerCase().replace(" ", "");
        Object value = locators.get(key);
        return value != null ? value.toString() : null;
    }

    // Retrieve a nested locator if the locators file has a nested structure
    public static String getNestedLocator(String parentKey, String childKey) {
        logger.debug("Locator to find from YML file for Page: {} and Object: {}", parentKey, childKey);
        Object parent = locators.get(parentKey);
        if (parent instanceof Map) {
            Object childValue = ((Map<?, ?>) parent).get(childKey.replace(" ", ""));
            return childValue != null ? childValue.toString() : null;
        }
        return null;
    }
}
