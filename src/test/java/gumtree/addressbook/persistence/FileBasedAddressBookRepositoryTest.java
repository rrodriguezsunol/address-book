package gumtree.addressbook.persistence;

import java.util.ArrayList;
import java.util.List;

import gumtree.addressbook.domain.Contact;
import gumtree.addressbook.domain.Gender;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class FileBasedAddressBookRepositoryTest {

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

        Contact bill = new Contact("Bill McKnight", Gender.MALE, "16/03/77");
        assertThat(actualContactList).containsExactly(bill);
    }

    @Test
    public void findAllReturnsSingleFemaleContactInTheAddressBook() {
        FileBasedAddressBookRepository addressBook = new FileBasedAddressBookRepository("AddressBookWithSarahOnly");

        List<Contact> actualContactList = addressBook.findAll();

        Contact sarah = new Contact("Sarah Stone", Gender.FEMALE, "20/09/80");
        assertThat(actualContactList).containsExactly(sarah);
    }

    @Test
    public void findAllReturnsAllContactsInTheAddressBook() {
        FileBasedAddressBookRepository addressBook = new FileBasedAddressBookRepository("AddressBookWithMultipleContacts");

        List<Contact> actualContactList = addressBook.findAll();

        Contact paul = new Contact("Paul Robinson", Gender.MALE, "15/01/85");
        Contact gemma = new Contact("Gemma Lane", Gender.FEMALE, "20/11/91");
        Contact wes = new Contact("Wes Jackson", Gender.MALE, "14/08/74");
        assertThat(actualContactList).containsExactly(paul, gemma, wes);
    }

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
    public void findAllReturnsACopyOfContactList() {
        FileBasedAddressBookRepository addressBook = new FileBasedAddressBookRepository("AddressBookWithSarahOnly");

        List<Contact> firstFindAll = addressBook.findAll();

        List<Contact> copyOfFirstFindAll = new ArrayList<>(firstFindAll);

        firstFindAll.add(new Contact("New contact", Gender.MALE, "01/01/89"));

        List<Contact> secondFindAll = addressBook.findAll();
        assertThat(secondFindAll).isEqualTo(copyOfFirstFindAll);
    }

    // Todo: add test for missing columns

    // Todo: add test for incorrect gender
}