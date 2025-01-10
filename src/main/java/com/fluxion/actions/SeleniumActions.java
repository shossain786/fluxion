package com.fluxion.actions;

import com.fluxion.utils.LocatorLoader;
import com.fluxion.utils.YamlLoader;
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
 */
public class SeleniumActions {
    private static final Logger logger = LoggerFactory.getLogger(SeleniumActions.class);
    private final WebDriver driver;
    private final Actions actions;
    /**
     * Constructor to initialize SeleniumActions with the WebDriver instance.
     *
     * @param driver WebDriver instance.
     */
    public SeleniumActions(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
    }

    /**
     * Navigate to a specified URL.
     *
     * @param url the URL to navigate to
     */
    public void navigateTo(String url) {
        try {
            logger.info("Navigating to URL: {}", url);
            driver.get(url);
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
            Map<String, String> locatorData = LocatorLoader.getLocator(locatorName);
            String locatorType = locatorData.get("type");
            String locatorValue = locatorData.get("value");

            return switch (locatorType.toLowerCase()) {
                case "id" -> driver.findElement(By.id(locatorValue));
                case "xpath" -> driver.findElement(By.xpath(locatorValue));
                case "css" -> driver.findElement(By.cssSelector(locatorValue));
                case "name" -> driver.findElement(By.name(locatorValue));
                case "linktext" -> driver.findElement(By.linkText(locatorValue));
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
    public void checkOrUncheck(String locatorName) {
        try {
            logger.info("Click to perform on field: {}", locatorName);
            WebElement element = findElement(locatorName);
            element.click();
        } catch (Exception e) {
            logger.error("Failed to click on field: {}. \nexception: {}", locatorName, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Select a value from a dropdown.
     *
     * @param section   the YAML section
     * @param fieldName the dropdown field name
     * @param value     the value to select
     */
    public void selectFromDropdown(String section, String fieldName, String value) {
        try {
            logger.info("Selecting value: '{}' from dropdown: {} in section: {}", value, fieldName, section);
            WebElement dropdown = findElement(section, fieldName);
            Select select = new Select(dropdown); // Added Select to handle dropdowns properly
            select.selectByVisibleText(value); // Selecting value by visible text
            logger.debug("Value: '{}' successfully selected from dropdown: {} in section: {}", value, fieldName, section);
        } catch (Exception e) {
            logger.error("Failed to select value: '{}' from dropdown: {} in section: {}", value, fieldName, section, e);
            throw new RuntimeException("Error selecting value from dropdown", e);
        }
    }

    /**
     * Hover over an element and verify its text.
     *
     * @param section       the YAML section
     * @param fieldName     the field name
     * @param expectedText  the expected text
     */
    public void hoverAndVerifyText(String section, String fieldName, String expectedText) {
        try {
            logger.info("Hovering over field: {} in section: {} and verifying text: {}", fieldName, section, expectedText);
            WebElement element = findElement(section, fieldName);
            actions.moveToElement(element).perform();
            String actualText = element.getText().trim(); // Trimming whitespace for comparison
            if (!actualText.equals(expectedText)) {
                logger.warn("Text mismatch! Expected: '{}', Found: '{}'", expectedText, actualText);
                throw new AssertionError("Text mismatch! Expected: '" + expectedText + "', Found: '" + actualText + "'");
            }
            logger.debug("Hover and text verification successful for field: {} in section: {}", fieldName, section);
        } catch (Exception e) {
            logger.error("Failed to hover and verify text on field: {} in section: {}", fieldName, section, e);
            throw new RuntimeException("Error during hover and text verification", e);
        }
    }

    /**
     * Drag and drop an element from one field to another.
     *
     * @param sourceSection the YAML section for the source
     * @param sourceField   the source field
     * @param targetSection the YAML section for the target
     * @param targetField   the target field
     */
    public void dragAndDrop(String sourceSection, String sourceField, String targetSection, String targetField) {
        try {
            logger.info("Dragging field: {} in section: {} to field: {} in section: {}", sourceField, sourceSection, targetField, targetSection);
            WebElement source = findElement(sourceSection, sourceField);
            WebElement target = findElement(targetSection, targetField);
            actions.dragAndDrop(source, target).perform();
            logger.debug("Drag and drop successful from field: {} in section: {} to field: {} in section: {}", sourceField, sourceSection, targetField, targetSection);
        } catch (Exception e) {
            logger.error("Failed to drag field: {} in section: {} to field: {} in section: {}", sourceField, sourceSection, targetField, targetSection, e);
            throw new RuntimeException("Error during drag and drop operation", e);
        }
    }

    /**
     * Find an element by its locator from YAML.
     *
     * @param section   the YAML section
     * @param fieldName the field name
     * @return the WebElement
     */
    private WebElement findElement(String section, String fieldName) {
        try {
            logger.debug("Fetching locator for field: {} in section: {}", fieldName, section);
            String locator = YamlLoader.getLocator(section, fieldName);

            // Adjust locator type as per your project's default, e.g., CSS Selector.
            By by = By.cssSelector(locator);

            WebElement element = driver.findElement(by);
            logger.debug("Element located for field: {} in section: {}", fieldName, section);
            return element;
        } catch (Exception e) {
            logger.error("Failed to find element for field: {} in section: {}", fieldName, section, e);
            throw new RuntimeException("Error finding element", e);
        }
    }



}
