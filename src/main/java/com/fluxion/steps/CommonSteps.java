package com.fluxion.steps;

import com.fluxion.core.ConfigManager;
import com.fluxion.core.DriverManager;
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
    }

    @Given("I open the login page")
    public void iOpenTheLoginPage() {
        try {
            String baseUrl = ConfigManager.getConfig("baseUrl");
            driver.get(baseUrl);

            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "OpenLoginPage");
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "OpenLoginPage_Error");
           throw new RuntimeException("Error in opening login page", e);
        }
    }

    @Then("I enter username and password")
    public void iEnterUsernameAndPassword() {
        try {
            System.out.println("Title of the page: " + driver.getTitle());

            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "EnterCredentials");
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "EnterCredentials_Error");
          throw new RuntimeException("Error in entering username and password", e);
        }
    }

    @And("I close the browser")
    public void iCloseTheBrowser() {
        try {
            DriverManager.quitDriver();
        } catch (Exception e) {
            throw new RuntimeException("Error in closing the browser", e);
        }
    }
}
