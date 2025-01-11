Feature: demo login test

  Background:
    Given I open the login page

  Scenario: Simple login test
    When I am on the "Login Page"
    And  I enter "admin" in "User Name"
    And  I enter "admin" in "Password"
    And  I click on "Login"
    Then I am on the "Home Page"
    Then I should see ""
    When I enter "Saddam Hossain" in "Full Name"
    And  I close the browser