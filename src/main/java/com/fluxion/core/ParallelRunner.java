package com.fluxion.core;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",  // Path to your feature files
        glue = "com.fluxion.steps",  // Path to your step definitions
        plugin = {"pretty", "html:target/cucumber-reports.html"},  // Reporting options
        monochrome = true  // Makes the console output readable
)
public class ParallelRunner extends AbstractTestNGCucumberTests {
    // No need for ParallelMode annotation here
}
