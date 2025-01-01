package com.fluxion.steps;

import com.fluxion.core.ConfigManager;
import com.fluxion.core.DriverManager;
import com.fluxion.report.ReportManager;
import com.fluxion.report.Step;
import com.fluxion.utils.ScreenshotUtil;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;

public class CommonSteps {

    private WebDriver driver;
    private Scenario scenario;

    @Before
    public void setUp(Scenario scenario) {
        this.scenario = scenario;
        String browser = ConfigManager.getConfig("browser");
        DriverManager.initializeDriver(browser);
        driver = DriverManager.getDriver();

        // Start a new scenario in the report
        String scenarioName = scenario.getName();
        ReportManager.startScenario(scenarioName);
    }

    @Given("I open the login page")
    public void iOpenTheLoginPage() {
        try {
            String baseUrl = ConfigManager.getConfig("baseUrl");
            driver.get(baseUrl);

            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "OpenLoginPage");
            ReportManager.getCurrentScenario().addStep(new Step("Opened login page: " + baseUrl, "PASS", screenshotPath));
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "OpenLoginPage_Error");
            ReportManager.getCurrentScenario().addStep(new Step("Failed to open login page", "FAIL", screenshotPath));
            throw new RuntimeException("Error in opening login page", e);
        }
    }

    @Then("I enter username and password")
    public void iEnterUsernameAndPassword() {
        try {
            // Simulated username and password entry
            System.out.println("Title of the page: " + driver.getTitle());

            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "EnterCredentials");
            ReportManager.getCurrentScenario().addStep(new Step("Entered username and password", "PASS", screenshotPath));
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "EnterCredentials_Error");
            ReportManager.getCurrentScenario().addStep(new Step("Failed to enter username and password", "FAIL", screenshotPath));
            throw new RuntimeException("Error in entering username and password", e);
        }
    }

    @And("I close the browser")
    public void iCloseTheBrowser() {
        try {
            DriverManager.quitDriver();
            ReportManager.getCurrentScenario().addStep(new Step("Closed the browser", "PASS", null));
        } catch (Exception e) {
            ReportManager.getCurrentScenario().addStep(new Step("Failed to close the browser", "FAIL", null));
            throw new RuntimeException("Error in closing the browser", e);
        }
    }
}
