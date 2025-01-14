Feature: demo login test

  Background:
    Given I navigate to url "testPageUrl"
    When I am on the "Login Page" screen
    And  I enter "admin" in "User Name"
    And  I enter "admin" in "Password"
    And  I click on "Login"

  Scenario: Test Full Name Filed
    When I am on the "Home Page" screen
    And  I should see ""
    When I enter "Saddam Hossain" in "Full Name"
    And  I close the browser

  Scenario: Test Career dropdown
    When I am on the "Home Page" screen
    And I select "Automation Tester" from "Carrier"
    And I close the browser