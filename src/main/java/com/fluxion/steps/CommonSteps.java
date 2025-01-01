package com.fluxion.steps;

import com.fluxion.core.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class CommonSteps {
    @Given("I open the login page")
    public void iOpenTheLoginPage() {
        DriverManager.getDriver().get("https://google.com/login");
    }

    @Then("I enter username and password")
    public void iEnterUsernameAndPassword() {
        System.out.println("Title of the page: " + DriverManager.getDriver().getTitle());
    }

    @And("I close the browser")
    public void iCloseTheBrowser() {
        DriverManager.quitDriver();
    }
}
