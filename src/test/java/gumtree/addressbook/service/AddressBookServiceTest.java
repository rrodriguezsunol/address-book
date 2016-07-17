package gumtree.addressbook.service;

import java.time.LocalDate;
import java.util.List;

import gumtree.addressbook.domain.Contact;
import gumtree.addressbook.domain.Gender;
import gumtree.addressbook.persistence.AddressBookRepository;
import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddressBookServiceTest {

    private AddressBookRepository mockAddressBookRepository = mock(AddressBookRepository.class);

    private AddressBookService addressBookService = new AddressBookService(mockAddressBookRepository);

    @Test
    public void countFemalesReturnsZeroWhenRepositoryReturnsEmptyList() {
        when(mockAddressBookRepository.findAll()).thenReturn(emptyList());

        int actualCount = addressBookService.countByGender(Gender.FEMALE);

        assertThat(actualCount).isZero();
    }

    @Test
    public void countMalesReturnsZeroWhenRepositoryReturnsEmptyList() {
        when(mockAddressBookRepository.findAll()).thenReturn(emptyList());

        int actualCount = addressBookService.countByGender(Gender.MALE);

        assertThat(actualCount).isZero();
    }

    @Test
    public void countFemalesReturnsOneWhenRepositoryReturnsListWithOneFemale() {
        List<Contact> singleFemaleContactList = singletonList(new Contact("Sara Stone", Gender.FEMALE, LocalDate.now()));
        when(mockAddressBookRepository.findAll()).thenReturn(singleFemaleContactList);

        int actualCount = addressBookService.countByGender(Gender.FEMALE);

        assertThat(actualCount).isEqualTo(1);
    }

    @Test
    public void countMalesReturnsOneWhenRepositoryReturnsListWithOneMale() {
        List<Contact> singleFemaleContactList = singletonList(new Contact("John Doe", Gender.MALE, LocalDate.now()));
        when(mockAddressBookRepository.findAll()).thenReturn(singleFemaleContactList);

        int actualCount = addressBookService.countByGender(Gender.MALE);

        assertThat(actualCount).isEqualTo(1);
    }

    @Test
    public void countFemalesReturnsFemaleCountWhenRepositoryReturnsListWithMixedGenders() {
        List<Contact> mixedGenderContactList = asList(
                new Contact("Sara Stone", Gender.FEMALE, LocalDate.now()),
                new Contact("John Doe", Gender.MALE, LocalDate.now()),
                new Contact("Gemma Lane", Gender.FEMALE, LocalDate.now()));
        when(mockAddressBookRepository.findAll()).thenReturn(mixedGenderContactList);

        int actualCount = addressBookService.countByGender(Gender.FEMALE);

        assertThat(actualCount).isEqualTo(2);
    }

    @Test
    public void countMalesReturnsMaleCountWhenRepositoryReturnsListWithMixedGenders() {
        List<Contact> mixedGenderContactList = asList(
                new Contact("Tom Ford", Gender.MALE, LocalDate.now()),
                new Contact("Sara Stone", Gender.FEMALE, LocalDate.now()),
                new Contact("John Doe", Gender.MALE, LocalDate.now()));
        when(mockAddressBookRepository.findAll()).thenReturn(mixedGenderContactList);

        int actualCount = addressBookService.countByGender(Gender.MALE);

        assertThat(actualCount).isEqualTo(2);
    }

    @Test
    public void countByGenderThrowsExceptionWhenGenderIsNull() {

        Throwable caughtException = catchThrowable(() -> addressBookService.countByGender(null));

        assertThat(caughtException).isExactlyInstanceOf(NullPointerException.class);
        assertThat(caughtException.getMessage()).isEqualTo("gender cannot be null");
    }

    @Test
    public void findOldestPeopleReturnsEmptyWhenThereAreNoContacts() {
        when(mockAddressBookRepository.findAll()).thenReturn(emptyList());

        List<Contact> actualOptionalContact = addressBookService.findOldestPeople();

        assertThat(actualOptionalContact).isEmpty();
    }

    @Test
    public void findOldestPeopleReturnsOldestPerson() {
        Contact oldestPerson = new Contact("Sara Stone", Gender.FEMALE, LocalDate.of(1980, 12, 1));
        List<Contact> contactList = asList(
                new Contact("Tom Ford", Gender.MALE, LocalDate.of(1980, 12, 2)),
                oldestPerson);
        when(mockAddressBookRepository.findAll()).thenReturn(contactList);

        List<Contact> actualOptionalContact = addressBookService.findOldestPeople();

        assertThat(actualOptionalContact).containsExactly(oldestPerson);
    }

    @Test
    public void findOldestPersonWhenTwoOrMorePeopleHaveTheSameAge() {
        Contact oldestPersonOne = new Contact("Tom Ford", Gender.MALE, LocalDate.of(1980, 12, 1));
        Contact oldestPersonTwo = new Contact("Sara Stone", Gender.FEMALE, LocalDate.of(1980, 12, 1));
        List<Contact> contactListWithTwoPeopleWithTheSameAge = asList(
                new Contact("Bill MccKnight", Gender.FEMALE, LocalDate.of(1985, 12, 1)),
                oldestPersonOne,
                oldestPersonTwo);
        when(mockAddressBookRepository.findAll()).thenReturn(contactListWithTwoPeopleWithTheSameAge);

        List<Contact> actualOldestPeople = addressBookService.findOldestPeople();

        assertThat(actualOldestPeople).containsOnly(oldestPersonTwo, oldestPersonOne);
    }
}