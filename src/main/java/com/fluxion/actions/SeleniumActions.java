package com.fluxion.actions;

import com.fluxion.utils.LocatorLoader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * SeleniumActions provides reusable methods for common Selenium operations.
 * Ensures thread safety for WebDriver and Actions instances.
 */
public class SeleniumActions {
    private static final Logger logger = LoggerFactory.getLogger(SeleniumActions.class);
    private static final ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();
    private static final ThreadLocal<Actions> threadActions = ThreadLocal.withInitial(() -> new Actions(threadDriver.get()));

    /**
     * Constructor to initialize SeleniumActions with the WebDriver instance.
     *
     * @param driver WebDriver instance.
     */
    public SeleniumActions(WebDriver driver) {
        threadDriver.set(driver);
    }

    /**
     * Navigate to a specified URL.
     *
     * @param url the URL to navigate to
     */
    public void navigateTo(String url) {
        try {
            logger.info("Navigating to URL: {}", url);
            getDriver().get(url);
        } catch (Exception e) {
            logger.error("Failed to navigate to URL: {}", url, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds a web element using the specified locator name.
     *
     * @param locatorName Name of the locator in the YAML file.
     * @return The located WebElement.
     */
    private WebElement findElement(String locatorName) {
        try {
            String locatorData = LocatorLoader.getLocator("",locatorName);
            String locatorType = locatorData.split("__")[0];
            String locatorValue = locatorData.split("__")[1];

            return switch (locatorType.toLowerCase()) {
                case "id" -> getDriver().findElement(By.id(locatorValue));
                case "xpath" -> getDriver().findElement(By.xpath(locatorValue));
                case "css" -> getDriver().findElement(By.cssSelector(locatorValue));
                case "name" -> getDriver().findElement(By.name(locatorValue));
                case "linktext" -> getDriver().findElement(By.linkText(locatorValue));
                default -> throw new RuntimeException("Unsupported locator type: " + locatorType);
            };
        } catch (Exception e) {
            throw new RuntimeException("Failed to find element: " + locatorName, e);
        }
    }

    /**
     * Enters text into a web element identified by the specified locator name.
     *
     * @param locatorName Name of the locator in the YAML file.
     * @param text        The text to enter.
     */
    public void enterText(String locatorName, String text) {
        try {
            logger.info("Entering text: '{}' in field: {}", text, locatorName);
            WebElement element = findElement(locatorName);
            element.clear();
            element.sendKeys(text);
        } catch (Exception e) {
            logger.error("Failed to enter text: '{}' in field: {}", text, locatorName, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Clicks on a web element identified by the specified locator name.
     *
     * @param locatorName Name of the locator in the YAML file.
     */
    public void click(String locatorName) {
        try {
            logger.info("Clicking on field: {}", locatorName);
            WebElement element = findElement(locatorName);
            element.click();
        } catch (Exception e) {
            logger.error("Failed to click on field: {}", locatorName, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Select a value from a dropdown.
     *
     * @param fieldName the dropdown field name
     * @param value     the value to select
     */
    public void selectFromDropdown(String fieldName, String value) {
        try {
            logger.info("Selecting value: '{}' from dropdown: {}", value, fieldName);
            WebElement dropdown = findElement(fieldName);
            Select select = new Select(dropdown);
            select.selectByVisibleText(value);
        } catch (Exception e) {
            logger.error("Failed to select value: '{}' from dropdown: {}", value, fieldName, e);
            throw new RuntimeException("Error selecting value from dropdown", e);
        }
    }

    /**
     * Get the thread-safe WebDriver instance.
     *
     * @return WebDriver instance.
     */
    private WebDriver getDriver() {
        WebDriver driver = threadDriver.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized for the current thread.");
        }
        return driver;
    }

    /**
     * Get the thread-safe Actions instance.
     *
     * @return Actions instance.
     */
    private Actions getActions() {
        return threadActions.get();
    }

    /**
     * Clean up the WebDriver and Actions instances for the current thread.
     */
    public static void cleanup() {
        threadDriver.remove();
        threadActions.remove();
    }
}
