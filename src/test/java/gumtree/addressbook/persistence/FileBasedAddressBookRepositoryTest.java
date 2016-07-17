package gumtree.addressbook.persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import gumtree.addressbook.domain.Contact;
import gumtree.addressbook.domain.Gender;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class FileBasedAddressBookRepositoryTest {

    @Test
    public void constructorThrowsExceptionWhenAddressBookPathDoesNotExist() {

        Throwable caughtException = catchThrowable(() -> new FileBasedAddressBookRepository("does not exist"));

        assertThat(caughtException).isExactlyInstanceOf(IllegalArgumentException.class);
        assertThat(caughtException.getMessage()).isEqualTo("Address book file does not exist");
    }

    @Test
    public void constructorThrowsExceptionWhenAddressBookPathIsNull() {

        Throwable caughtException = catchThrowable(() -> new FileBasedAddressBookRepository(null));

        assertThat(caughtException).isExactlyInstanceOf(NullPointerException.class);
        assertThat(caughtException.getMessage()).isEqualTo("addressBookFilePath cannot be null");
    }

    @Test
    public void constructorThrowsExceptionWhenThereIsARecordWithLessThanThreeColumns() {

        Throwable caughtException = catchThrowable(() -> new FileBasedAddressBookRepository("AddressBookWithOneRecordWithoutFullName"));

        assertThat(caughtException).isExactlyInstanceOf(PersistenceException.class);
        assertThat(caughtException.getMessage()).isEqualTo("Line 1 is invalid. There are missing fields");
    }

    @Test
    public void constructorThrowsExceptionWhenThereIsARecordWithInvalidGenderValue() {
        Throwable caughtException = catchThrowable(() -> new FileBasedAddressBookRepository("AddressBookWithOneRecordWithInvalidGender"));

        assertThat(caughtException).isExactlyInstanceOf(PersistenceException.class);
        assertThat(caughtException.getMessage())
                .isEqualTo("Line 2 is invalid. Gender must be one of \"Male\" or \"Female\". value=\"invalid gender\"");
    }

    @Test
    public void constructorThrowsExceptionWhenDateOfBirthHasInvalidFormat() {
        Throwable caughtException = catchThrowable(() -> new FileBasedAddressBookRepository("AddressBookWithOneRecordWithInvalidDateOfBirth"));

        assertThat(caughtException).isExactlyInstanceOf(PersistenceException.class);
        assertThat(caughtException.getMessage())
                .isEqualTo("Line 3 is invalid. Date of birth must have the format \"dd/MM/yy\". value=\"invalid dob\"");
    }

    @Test
    public void findAllReturnsEmptyListWhenAddressBookIsEmpty() {
        FileBasedAddressBookRepository addressBook = new FileBasedAddressBookRepository("EmptyAddressBook");

        List<Contact> actualContactList = addressBook.findAll();

        assertThat(actualContactList).isEmpty();
    }

    @Test
    public void findAllReturnsSingleMaleContactInTheAddressBook() {
        FileBasedAddressBookRepository addressBook = new FileBasedAddressBookRepository("AddressBookWithBillOnly");

        List<Contact> actualContactList = addressBook.findAll();

        Contact bill = new Contact("Bill McKnight", Gender.MALE, LocalDate.of(1977, 3, 16));
        assertThat(actualContactList).containsExactly(bill);
    }

    @Test
    public void findAllReturnsSingleFemaleContactInTheAddressBook() {
        FileBasedAddressBookRepository addressBook = new FileBasedAddressBookRepository("AddressBookWithSarahOnly");

        List<Contact> actualContactList = addressBook.findAll();

        Contact sarah = new Contact("Sarah Stone", Gender.FEMALE, LocalDate.of(1980, 9, 20));
        assertThat(actualContactList).containsExactly(sarah);
    }

    @Test
    public void findAllReturnsAllContactsInTheAddressBook() {
        FileBasedAddressBookRepository addressBook = new FileBasedAddressBookRepository("AddressBookWithMultipleContacts");

        List<Contact> actualContactList = addressBook.findAll();

        Contact paul = new Contact("Paul Robinson", Gender.MALE, LocalDate.of(1985, 1, 15));
        Contact gemma = new Contact("Gemma Lane", Gender.FEMALE, LocalDate.of(1991, 11, 20));
        Contact wes = new Contact("Wes Jackson", Gender.MALE, LocalDate.of(1974, 8, 14));
        assertThat(actualContactList).containsExactly(paul, gemma, wes);
    }

    @Test
    public void findAllReturnsACopyOfContactList() {
        FileBasedAddressBookRepository addressBook = new FileBasedAddressBookRepository("AddressBookWithSarahOnly");

        List<Contact> firstFindAll = addressBook.findAll();

        List<Contact> copyOfFirstFindAll = new ArrayList<>(firstFindAll);

        firstFindAll.add(new Contact("New contact", Gender.MALE, LocalDate.now()));

        List<Contact> secondFindAll = addressBook.findAll();
        assertThat(secondFindAll).isEqualTo(copyOfFirstFindAll);
    }
}