package com.fluxion.runner;

import com.fluxion.utils.LocatorLoader;
import com.fluxion.utils.YamlReader;

import java.io.File;
import java.net.URL;

/**
 * ProjectSetup is responsible for initializing project-wide settings, configurations, and resources.
 */
public class ProjectSetup {

    private static final String DEFAULT_LOCATOR_PATH = "mylocators.yml"; // Path to the single YAML file (update as per actual file)

    /**
     * Initializes the project by loading configuration and locator data.
     *
     * @param path The path to the configuration file, can be null to use default locators.
     */
    public static void initializeProject(String path) {
        // Use class loader to find the resource if no path is provided
        final String finalPath = (path == null || path.isEmpty()) ? DEFAULT_LOCATOR_PATH : path;

        // Load configuration file if provided
        URL configUrl = ProjectSetup.class.getClassLoader().getResource(finalPath);
        if (configUrl == null) {
            throw new RuntimeException("Failed to find resource: " + finalPath);
        }

        // Assuming YamlReader expects a URL or path, load the YAML file
        YamlReader.loadYamlFile(configUrl.getPath());

        // Load the locator data from the YAML configuration (if applicable)
        // You can process the YAML file's content as needed. Here, it's assumed that YamlReader
        // has a method to handle locator-specific logic based on the loaded configuration.
        // If the YAML content has locator directories or other data, process it accordingly.

        // Example: Assuming the YAML file may have locators in a specific format.
        // Example: Map<String, String> locatorDirs = YamlReader.getLocatorDirectories();
        // Here, you can process individual locators if required. Otherwise, if the file is simple,
        // you don't need to load locators separately.

        System.out.println("Project initialized using config file: " + finalPath);
    }

}
