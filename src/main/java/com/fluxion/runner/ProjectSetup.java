package com.fluxion.runner;

import com.fluxion.utils.LocatorLoader;
import com.fluxion.utils.YamlReader;

import java.util.Map;

/**
 * ProjectSetup is responsible for initializing project-wide settings, configurations, and resources.
 */
public class ProjectSetup {

    /**
     * Initializes the project by loading configuration and locator data.
     */
    public static void initializeProject(String path) {
        // Load configuration file
        YamlReader.loadYamlFile(path);

        // Load all locators from specified directories
        Map<String, String> locatorDirs = YamlReader.getLocatorDirectories();
        locatorDirs.values().forEach(LocatorLoader::loadLocators);
    }
}
