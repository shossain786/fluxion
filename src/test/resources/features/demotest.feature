Feature: demo login test

  Background:
    Given I navigate to url "testPageUrl"

  Scenario: Simple login test
    When I am on the "Login Page" screen
    And  I enter "admin" in "User Name"
    And  I enter "admin" in "Password"
    And  I click on "Login"
    Then I am on the "Home Page" screen
    And  I should see ""
    When I enter "Saddam Hossain" in "Full Name"
    And  I close the browser