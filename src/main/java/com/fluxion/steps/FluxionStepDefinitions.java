package com.fluxion.steps;

import com.fluxion.actions.SeleniumActions;
import com.fluxion.core.ConfigManager;
import com.fluxion.core.DriverManager;
import com.fluxion.utils.FluxionConstants;
import com.fluxion.utils.ScreenshotUtil;
import com.fluxion.utils.ThreadSafeMemory;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FluxionStepDefinitions - Merged step definitions for Fluxion UI testing with enhanced logging, thread safety, and dynamic locator handling.
 *
 * <p>This class provides step definitions for UI tests, including navigation, clicking elements, form data entry, and verification. It also includes enhanced error handling with screenshot capture.</p>
 */
public class FluxionStepDefinitions {
    private static final Logger logger = LogManager.getLogger(FluxionStepDefinitions.class);

    // Thread-local variables for WebDriver, Scenario, and current page name
    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
    private static final ThreadLocal<Scenario> threadLocalScenario = new ThreadLocal<>();
    private static final ThreadLocal<String> currentPageName = ThreadLocal.withInitial(() -> null);
    SeleniumActions seleniumActions = new SeleniumActions();
    /**
     * Retrieves the WebDriver instance for the current thread.
     *
     * @return The WebDriver instance.
     */
    private WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    /**
     * Retrieves the Scenario instance for the current thread.
     *
     * @return The Scenario instance.
     */
    private Scenario getScenario() {
        return threadLocalScenario.get();
    }

    /**
     * Sets up the WebDriver and Scenario for the current thread.
     *
     * @param scenario The Cucumber scenario instance.
     */
    @Before
    public void setUp(Scenario scenario) {
        threadLocalScenario.set(scenario);
        String browser = ConfigManager.getConfig("browser");
        DriverManager.initializeDriver(browser);
        threadLocalDriver.set(DriverManager.getDriver());
    }

    /**
     * Step to navigate to a specific page and set the current page name.
     *
     * @param pageName The name of the page to navigate to.
     */
    @Given("I am on the {string} screen")
    public void iAmOnThePage(String pageName) {
        pageName = pageName.replace(" ", "");
        currentPageName.set(pageName);
        ThreadSafeMemory.put(FluxionConstants.CURRENT_PAGE_NAME, pageName);
        logger.debug("I am on the: {}", pageName);
    }

    @Given("I navigate to url {string}")
    public void iNavigateToUrl(String urlKey) {
        try {
            String url = ConfigManager.getConfig(urlKey);
            assert  url != null;
            getDriver().get(url);
            String screenshotPath = ScreenshotUtil.captureScreenshot(getDriver(), urlKey);
            getScenario().log("Application page opened. Screenshot: " + screenshotPath);
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(getDriver(), "OpenLoginPage_Error");
            getScenario().log("Error in opening application page. Screenshot: " + screenshotPath);
            throw new RuntimeException("Error in opening login page", e);
        }
    }

    /**
     * Step to click on a specific field on the current page using dynamic locators.
     *
     * @param fieldName The name of the field to click on.
     */
    @And("I click on {string}")
    public void iClickOn(String fieldName) {
        String pageName = currentPageName.get();
        if (pageName == null) {
            throw new RuntimeException("Current page name is not set. Use 'I am on the \"<pageName>\"' step first.");
        }

        try {
            seleniumActions.click(fieldName);
            logger.debug("Clicked on: {} on page: {}", fieldName, pageName);
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(getDriver(), "ClickError_" + fieldName);
            logger.error("Error clicking on: {} on page: {}. Screenshot: {}", fieldName, pageName, screenshotPath);
            throw new RuntimeException("Failed to click on field: " + fieldName + " on page: " + pageName, e);
        }
    }

    /**
     * Step to enter data into a form field dynamically using locators from the configuration file.
     *
     * @param data The data to be entered.
     * @param fieldName The name of the field.
     */
    @And("I enter {string} in {string}")
    public void iEnterIn(String data, String fieldName) {
        try {
            seleniumActions.enterText(fieldName, data);
            logger.debug("Entered '{}' in field: '{}'", data, fieldName);
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(getDriver(), "EnterDataError_" + fieldName);
            logger.error("Error entering data in field: {}. Screenshot: {}", fieldName, screenshotPath);
            throw new RuntimeException("Failed to enter data in field: " + fieldName, e);
        }
    }

    /**
     * Step to verify that specific text is visible on the page.
     *
     * @param expectedText The text to verify.
     */
    @Then("I should see {string}")
    public void iShouldSee(String expectedText) {
        try {
            boolean textVisible = getDriver().getPageSource().contains(expectedText);
            if (!textVisible) {
                throw new AssertionError("Expected text not found: " + expectedText);
            }

            getScenario().log("Verified the presence of text: " + expectedText);
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(getDriver(), "TextVerificationError");
            getScenario().log("Error verifying text: " + expectedText + ". Screenshot: " + screenshotPath);
            throw new RuntimeException("Failed to verify text: " + expectedText, e);
        }
    }

    /**
     * Step to navigate to a new page during the test execution.
     *
     * @param newPageName The name of the new page to navigate to.
     */
    @Then("I navigate to the {string}")
    public void iNavigateTo(String newPageName) {
        currentPageName.set(newPageName);
        logger.debug("Navigated to page: {}", newPageName);
    }

    /**
     * Step to close the browser and clean up the WebDriver instance.
     */
    @And("I close the browser")
    public void iCloseTheBrowser() {
        try {
            DriverManager.quitDriver();
            threadLocalDriver.remove(); // Remove thread-local driver instance
            getScenario().log("Browser closed successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Error in closing the browser", e);
        }
    }

/**
 * Selects a specified value from a dropdown field.
 * <p>
 * Logs the action and interacts with the UI to select the provided value
 * from the dropdown identified by the field name.
 * </p>
 *
 * @param data      The value to select from the dropdown.
 * @param fieldName The name or identifier of the dropdown field.
 */

 @And("I select {string} from {string}")
    public void iSelectFrom(String data, String fieldName) {
        logger.debug("I select {} from {}", data, fieldName);
        seleniumActions.selectFromDropdown(fieldName, data);
    }
}