# Fluxion: A Reusable Test Automation Library

Fluxion is a reusable library for test automation built with **Cucumber**, **Java**, and **Selenium**. This framework simplifies integration into multiple projects, enabling you to standardize test execution and management. It supports parallel execution, YAML-based locators, configurable reports, and more.

---

## Features
- **WebDriver Management**: Configurable WebDriver instances for Chrome, Firefox, etc.
- **Cucumber Integration**: BDD-style test case execution.
- **Parallel Execution**: Optimized for running tests in parallel using TestNG.
- **Configurable YAML Support**: Store locators and configurations in YAML files for easy updates.
- **HTML Report Generation**: Automatically generate detailed HTML reports after test execution.
- **Loosely Coupled Design**: Easily integrates with any project using Maven or Gradle dependencies.

---

## Project Structure
```
fluxion/
│
├── src/main/java/com/fluxion/
│   ├── core/            # Core reusable components
│   │   ├── DriverManager.java  # WebDriver management
│   │   ├── ConfigManager.java  # YAML configuration management
│   │   └── ParallelRunner.java # Parallel execution support
│   │
│   ├── steps/           # Common step definitions
│   │   └── CommonSteps.java
│   │
│   ├── utils/           # Utility classes
│   │   ├── YamlReader.java    # For reading YAML files
│   │   └── ReportManager.java # For HTML report generation
│   │
│   └── constants/       # Common constants
│       └── FrameworkConstants.java
│
├── src/test/resources/
│   ├── config/          # Config YAML files
│   │   └── config.yml
│   └── locators/        # Locators YAML files
│       └── example_locators.yml
│
├── pom.xml              # Maven configuration for dependencies
└── README.md            # Documentation
```
