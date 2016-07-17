package gumtree.addressbook.persistence;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import gumtree.addressbook.domain.Contact;
import gumtree.addressbook.domain.Gender;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class CsvAddressBookReaderTest {

    @Test
    public void readThrowsExceptionWhenThereIsARecordWithLessThanThreeColumns() {
        CsvAddressBookReader csvAddressBookReader = new CsvAddressBookReader(getResourceUrl("AddressBookWithOneRecordWithoutFullName"));

        Throwable caughtException = catchThrowable(csvAddressBookReader::read);

        assertThat(caughtException).isExactlyInstanceOf(PersistenceException.class);
        assertThat(caughtException).hasMessage("Line 1 is invalid. There are missing fields");
    }

    @Test
    public void readThrowsExceptionWhenThereIsARecordWithInvalidGenderValue() {
        CsvAddressBookReader csvAddressBookReader = new CsvAddressBookReader(getResourceUrl("AddressBookWithOneRecordWithInvalidGender"));

        Throwable caughtException = catchThrowable(csvAddressBookReader::read);

        assertThat(caughtException).isExactlyInstanceOf(PersistenceException.class);
        assertThat(caughtException).hasMessage("Line 2 is invalid. Gender must be one of \"Male\" or \"Female\". value=\"invalid gender\"");
    }

    @Test
    public void readThrowsExceptionWhenDateOfBirthHasInvalidFormat() {
        CsvAddressBookReader csvAddressBookReader = new CsvAddressBookReader(getResourceUrl("AddressBookWithOneRecordWithInvalidDateOfBirth"));

        Throwable caughtException = catchThrowable(csvAddressBookReader::read);

        assertThat(caughtException).isExactlyInstanceOf(PersistenceException.class);
        assertThat(caughtException).hasMessage("Line 3 is invalid. Date of birth must have the format \"dd/MM/yy\". value=\"invalid dob\"");
    }

    @Test
    public void readReturnsEmptyListWhenAddressBookIsEmpty() {
        CsvAddressBookReader csvAddressBookReader = new CsvAddressBookReader(getResourceUrl("EmptyAddressBook"));

        List<Contact> actualContactList = csvAddressBookReader.read();

        assertThat(actualContactList).isEmpty();
    }

    @Test
    public void readReturnsSingleMaleContactInTheAddressBook() {
        CsvAddressBookReader csvAddressBookReader = new CsvAddressBookReader(getResourceUrl("AddressBookWithBillOnly"));

        List<Contact> actualContactList = csvAddressBookReader.read();

        Contact bill = new Contact("Bill McKnight", Gender.MALE, LocalDate.of(1977, 3, 16));
        assertThat(actualContactList).containsExactly(bill);
    }

    @Test
    public void findAllReturnsSingleFemaleContactInTheAddressBook() {
        CsvAddressBookReader csvAddressBookReader = new CsvAddressBookReader(getResourceUrl("AddressBookWithSarahOnly"));

        List<Contact> actualContactList = csvAddressBookReader.read();

        Contact sarah = new Contact("Sarah Stone", Gender.FEMALE, LocalDate.of(1980, 9, 20));
        assertThat(actualContactList).containsExactly(sarah);
    }

    @Test
    public void findAllReturnsAllContactsInTheAddressBook() {
        CsvAddressBookReader csvAddressBookReader = new CsvAddressBookReader(getResourceUrl("AddressBookWithMultipleContacts"));

        List<Contact> actualContactList = csvAddressBookReader.read();

        Contact paul = new Contact("Paul Robinson", Gender.MALE, LocalDate.of(1985, 1, 15));
        Contact gemma = new Contact("Gemma Lane", Gender.FEMALE, LocalDate.of(1991, 11, 20));
        Contact wes = new Contact("Wes Jackson", Gender.MALE, LocalDate.of(1974, 8, 14));
        assertThat(actualContactList).containsExactly(paul, gemma, wes);
    }

    private URL getResourceUrl(String csvFilePathInJar) {
        return getClass().getClassLoader().getResource(csvFilePathInJar);
    }
}