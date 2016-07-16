package gumtree.addressbook;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class AddressBookAppSteps {
    private AddressBookApp addressBookApp;
    private int numberOfMales;

    @Given("^I launch the address book app$")
    public void iLaunchTheAddressBookApp() throws Throwable {
        addressBookApp = new AddressBookApp();
    }

    @When("^I ask for the total number of males$")
    public void iAskForTheTotalNumberOfMales() throws Throwable {
        numberOfMales = addressBookApp.countNumberOfMales();
    }

    @Then("^I get a total of (\\d+)$")
    public void iGetATotalOf(int totalExpected) throws Throwable {
        assertThat(numberOfMales).isEqualTo(totalExpected);
    }
}
