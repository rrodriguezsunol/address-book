package gumtree.addressbook.persistence;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import gumtree.addressbook.domain.Contact;
import gumtree.addressbook.domain.Gender;
import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class InMemoryAddressBookRepositoryTest {

    private Contact paul = new Contact("Paul Robinson", Gender.MALE, LocalDate.of(1985, 1, 15));
    private Contact gemma = new Contact("Gemma Lane", Gender.FEMALE, LocalDate.of(1991, 11, 20));
    private Contact wes = new Contact("Wes Jackson", Gender.MALE, LocalDate.of(1974, 8, 14));

    @Test
    public void constructorThrowsExceptionWhenListOfContactsIsNull() {

        Throwable caughtException = catchThrowable(() -> new InMemoryAddressBookRepository(null));

        assertThat(caughtException).isExactlyInstanceOf(NullPointerException.class);
        assertThat(caughtException).hasMessage("contacts cannot be null");
    }

    @Test
    public void findAllReturnsACopyOfContactList() {
        List<Contact> initialList = Collections.singletonList(new Contact("New contact", Gender.MALE, LocalDate.now()));
        InMemoryAddressBookRepository addressBook = new InMemoryAddressBookRepository(initialList);

        List<Contact> firstFindAll = addressBook.findAll();

        firstFindAll.add(new Contact("New contact", Gender.MALE, LocalDate.now()));

        List<Contact> secondFindAll = addressBook.findAll();
        assertThat(secondFindAll).isEqualTo(initialList);
    }

    @Test
    public void findByNameReturnsEmptyWhenAddressBookIsEmpty() {
        InMemoryAddressBookRepository addressBook = new InMemoryAddressBookRepository(emptyList());

        Optional<Contact> actualContact = addressBook.findByFullName("any name");

        assertThat(actualContact).isEmpty();
    }

    @Test
    public void findByNameReturnsContactWhenOneContactWithSameNameIsFound() {
        InMemoryAddressBookRepository addressBook = new InMemoryAddressBookRepository(asList(paul, gemma, wes));

        Optional<Contact> actualContact = addressBook.findByFullName("Gemma Lane");

        assertThat(actualContact).contains(gemma);
    }

    @Test
    public void findByNameThrowsExceptionWhenFullNameIsNull() {
        InMemoryAddressBookRepository addressBook = new InMemoryAddressBookRepository(asList(paul, gemma, wes));

        Throwable caughtException = catchThrowable(() -> addressBook.findByFullName(null));

        assertThat(caughtException).isExactlyInstanceOf(NullPointerException.class);
        assertThat(caughtException).hasMessage("fullName cannot be null");
    }

    // Todo: what to do when more than one contact have the same name?

    @Test
    public void findEarliestDateOfBirthReturnsEmptyWhenAddressBookIsEmpty() {
        InMemoryAddressBookRepository addressBook = new InMemoryAddressBookRepository(emptyList());

        Optional<LocalDate> actualDateOfBirth = addressBook.findEarliestDateOfBirth();

        assertThat(actualDateOfBirth).isEmpty();
    }

    @Test
    public void findEarliestDateOfBirthReturnsTheDateOfBirthOfTheOldestContact() {
        InMemoryAddressBookRepository addressBook = new InMemoryAddressBookRepository(asList(paul, gemma, wes));

        Optional<LocalDate> actualDateOfBirth = addressBook.findEarliestDateOfBirth();

        assertThat(actualDateOfBirth).contains(wes.getDateOfBirth());
    }

    @Test
    public void findByDateOfBirthReturnsEmptyWhenAddressBookIsEmpty() {
        InMemoryAddressBookRepository addressBook = new InMemoryAddressBookRepository(emptyList());

        List<Contact> actualContacts = addressBook.findByDateOfBirth(LocalDate.now());

        assertThat(actualContacts).isEmpty();
    }

    @Test
    public void findByDateOfBirthReturnsEmptyWhenNoContactInAddressBookMatchesGivenDate() {
        InMemoryAddressBookRepository addressBook = new InMemoryAddressBookRepository(asList(paul, gemma, wes));

        List<Contact> actualContacts = addressBook.findByDateOfBirth(LocalDate.now());

        assertThat(actualContacts).isEmpty();
    }

    @Test
    public void findByDateOfBirthReturnsSingleContactThatMatchesTheGivenDate() {
        InMemoryAddressBookRepository addressBook = new InMemoryAddressBookRepository(asList(paul, gemma, wes));

        List<Contact> actualContacts = addressBook.findByDateOfBirth(wes.getDateOfBirth());

        assertThat(actualContacts).containsExactly(wes);
    }

    @Test
    public void findByDateOfBirthReturnsAllContactThatMatchTheGivenDate() {
        Contact anotherContactWithSameDateOfBirthAsGemma = new Contact("new contact", Gender.MALE, gemma.getDateOfBirth());
        InMemoryAddressBookRepository addressBook = new InMemoryAddressBookRepository(asList(paul, gemma, wes, anotherContactWithSameDateOfBirthAsGemma));

        List<Contact> actualContacts = addressBook.findByDateOfBirth(gemma.getDateOfBirth());

        assertThat(actualContacts).containsOnly(gemma, anotherContactWithSameDateOfBirthAsGemma);
    }

    @Test
    public void findByDateOfBirthThrowsExceptionWhenDateOfBirthIsNull() {
        InMemoryAddressBookRepository addressBook = new InMemoryAddressBookRepository(asList(paul, gemma, wes));

        Throwable caughtException = catchThrowable(() -> addressBook.findByDateOfBirth(null));

        assertThat(caughtException).isExactlyInstanceOf(NullPointerException.class);
        assertThat(caughtException).hasMessage("dateOfBirth cannot be null");
    }
}