Feature: Address book application

  Scenario: Count number of contacts of a particular gender
    Given I launch the address book app
    When I ask for the total number of males
    Then I get a total of 3

