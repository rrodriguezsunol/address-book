package gumtree.addressbook;

import java.util.List;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class AddressBookAppSteps {
    private AddressBookApp addressBookApp;
    private int numberOfMales;
    private List<String> nameOfOldestPerson;
    private long ageDifferenceInDays;

    @Given("^I launch the address book app$")
    public void iLaunchTheAddressBookApp() {
        addressBookApp = AddressBookAppFactory.newInstance("AddressBook");
    }

    @When("^I ask for the total number of males$")
    public void iAskForTheTotalNumberOfMales() {
        numberOfMales = addressBookApp.countNumberOfMales();
    }

    @Then("^I get a total of (\\d+)$")
    public void iGetATotalOf(int totalExpected) {
        assertThat(numberOfMales).isEqualTo(totalExpected);
    }

    @When("^I ask for the oldest person$")
    public void iAskForTheOldestPerson() {
        nameOfOldestPerson = addressBookApp.getOldestPerson();
    }

    @Then("^the result is \"(.*?)\"$")
    public void theResultIs(String contactName) {
        assertThat(nameOfOldestPerson).containsExactly(contactName);
    }

    @When("^I ask for the age difference in days between Bill McKnight and Paul Robinson$")
    public void iAskForTheAgeDifferenceInDaysBetweenBillMcKnightAndPaulRobinson() {
        ageDifferenceInDays = addressBookApp.ageDifferenceInDaysBetweenBillAndPaul();
    }

    @Then("^the result is (\\d+)$")
    public void the_result_is(int expectedAgeDifference) {
        assertThat(ageDifferenceInDays).isEqualTo(expectedAgeDifference);
    }

}
