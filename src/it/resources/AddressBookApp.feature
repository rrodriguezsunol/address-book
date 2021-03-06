Feature: Address book application

  Scenario: Count number of contacts of a particular gender
    Given I launch the address book app
    When I ask for the total number of males
    Then I get a total of 3

  Scenario: Find the oldest person
    Given I launch the address book app
    When I ask for the oldest person
    Then the result is "Wes Jackson"

  Scenario: Age difference in days
    Given I launch the address book app
    When I ask for the age difference in days between Bill McKnight and Paul Robinson
    Then the result is 2862
