package gumtree.addressbook.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import gumtree.addressbook.domain.Contact;
import gumtree.addressbook.domain.Gender;
import gumtree.addressbook.persistence.AddressBookRepository;
import org.junit.Test;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class AddressBookServiceTest {

    private AddressBookRepository mockAddressBookRepository = mock(AddressBookRepository.class);

    private AddressBookService addressBookService = new AddressBookService(mockAddressBookRepository);

    @Test
    public void findOldestPeopleReturnsEmptyWhenThereIsNoEarliestDateOfBirth() {
        when(mockAddressBookRepository.findEarliestDateOfBirth()).thenReturn(Optional.empty());

        List<Contact> actualOptionalContact = addressBookService.findOldestPeople();

        assertThat(actualOptionalContact).isEmpty();
        verify(mockAddressBookRepository, never()).findByDateOfBirth(any(LocalDate.class));
    }

    @Test
    public void findOldestPeopleReturnsListOfContactsThatMatchTheGivenDateOfBirth() {
        LocalDate earliestDateOfBirth = LocalDate.of(1980, 12, 2);
        when(mockAddressBookRepository.findEarliestDateOfBirth()).thenReturn(Optional.of(earliestDateOfBirth));

        List<Contact> expectedOldestPeople = singletonList(new Contact("Tom Ford", Gender.MALE, earliestDateOfBirth));
        when(mockAddressBookRepository.findByDateOfBirth(earliestDateOfBirth)).thenReturn(expectedOldestPeople);

        List<Contact> actualOptionalContact = addressBookService.findOldestPeople();

        assertThat(actualOptionalContact).isEqualTo(expectedOldestPeople);
    }

    @Test
    public void ageDifferenceInDaysReturnsZeroWhenTheTwoPeopleWereBornOnTheSameDate() {
        Contact tom = new Contact("Tom Ford", Gender.MALE, LocalDate.of(1980, 12, 1));
        Contact sara = new Contact("Sara Stone", Gender.FEMALE, LocalDate.of(1980, 12, 1));
        when(mockAddressBookRepository.findByFullName(tom.getFullName())).thenReturn(Optional.of(tom));
        when(mockAddressBookRepository.findByFullName(sara.getFullName())).thenReturn(Optional.of(sara));

        long actualAgeDifferenceInDays = addressBookService.ageDifferenceInDays("Tom Ford", "Sara Stone");

        assertThat(actualAgeDifferenceInDays).isZero();
    }

    @Test
    public void ageDifferenceInDaysReturnsOneWhenTheFirstPersonIsOneDayYoungerThanSecond() {
        Contact tom = new Contact("Tom Ford", Gender.MALE, LocalDate.of(1980, 12, 2));
        Contact sara = new Contact("Sara Stone", Gender.FEMALE, LocalDate.of(1980, 12, 1));
        when(mockAddressBookRepository.findByFullName(tom.getFullName())).thenReturn(Optional.of(tom));
        when(mockAddressBookRepository.findByFullName(sara.getFullName())).thenReturn(Optional.of(sara));

        long actualAgeDifferenceInDays = addressBookService.ageDifferenceInDays("Tom Ford", "Sara Stone");

        assertThat(actualAgeDifferenceInDays).isEqualTo(1);
    }

    @Test
    public void ageDifferenceInDaysReturnsOneWhenTheFirstPersonIsOneDayOlderThanSecond() {
        Contact tom = new Contact("Tom Ford", Gender.MALE, LocalDate.of(1980, 12, 1));
        Contact sara = new Contact("Sara Stone", Gender.FEMALE, LocalDate.of(1980, 12, 2));
        when(mockAddressBookRepository.findByFullName(tom.getFullName())).thenReturn(Optional.of(tom));
        when(mockAddressBookRepository.findByFullName(sara.getFullName())).thenReturn(Optional.of(sara));

        long actualAgeDifferenceInDays = addressBookService.ageDifferenceInDays("Tom Ford", "Sara Stone");

        assertThat(actualAgeDifferenceInDays).isEqualTo(1);
    }

    @Test
    public void ageDifferenceInDaysReturnsThreeHundredSixtyFiveWhenThereIsAYearDifference() {
        Contact tom = new Contact("Tom Ford", Gender.MALE, LocalDate.of(2013, 1, 1));
        Contact sara = new Contact("Sara Stone", Gender.FEMALE, LocalDate.of(2014, 1, 1));
        when(mockAddressBookRepository.findByFullName(tom.getFullName())).thenReturn(Optional.of(tom));
        when(mockAddressBookRepository.findByFullName(sara.getFullName())).thenReturn(Optional.of(sara));

        long actualAgeDifferenceInDays = addressBookService.ageDifferenceInDays("Tom Ford", "Sara Stone");

        assertThat(actualAgeDifferenceInDays).isEqualTo(365);
    }

    @Test
    public void ageDifferenceInDaysTakesIntoAccountExtraDayInLeapYear() {
        Contact tom = new Contact("Tom Ford", Gender.MALE, LocalDate.of(2016, 2, 1));
        Contact sara = new Contact("Sara Stone", Gender.FEMALE, LocalDate.of(2016, 3, 1));
        when(mockAddressBookRepository.findByFullName(tom.getFullName())).thenReturn(Optional.of(tom));
        when(mockAddressBookRepository.findByFullName(sara.getFullName())).thenReturn(Optional.of(sara));

        long actualAgeDifferenceInDays = addressBookService.ageDifferenceInDays("Tom Ford", "Sara Stone");

        assertThat(actualAgeDifferenceInDays).isEqualTo(29);
    }

    @Test
    public void ageDifferenceThrowsExceptionWhenFirstPersonIsNotFoundInRepository() {
        Contact sara = new Contact("Sara Stone", Gender.FEMALE, LocalDate.of(1980, 12, 2));
        when(mockAddressBookRepository.findByFullName("not found")).thenReturn(Optional.empty());
        when(mockAddressBookRepository.findByFullName(sara.getFullName())).thenReturn(Optional.of(sara));

        Throwable caughtException = catchThrowable(() -> addressBookService.ageDifferenceInDays("not found", sara.getFullName()));

        assertThat(caughtException).isExactlyInstanceOf(IllegalArgumentException.class);
        assertThat(caughtException).hasMessage("firstPersonFullName not found");
    }

    @Test
    public void ageDifferenceThrowsExceptionWhenSecondPersonIsNotFoundInRepository() {
        Contact sara = new Contact("Sara Stone", Gender.FEMALE, LocalDate.of(1980, 12, 2));
        when(mockAddressBookRepository.findByFullName(sara.getFullName())).thenReturn(Optional.of(sara));
        when(mockAddressBookRepository.findByFullName("not found")).thenReturn(Optional.empty());

        Throwable caughtException = catchThrowable(() -> addressBookService.ageDifferenceInDays(sara.getFullName(), "not found"));

        assertThat(caughtException).isExactlyInstanceOf(IllegalArgumentException.class);
        assertThat(caughtException).hasMessage("secondPersonFullName not found");
    }

    @Test
    public void ageDifferenceThrowsExceptionWhenFirstAndSecondPersonAreNotFoundInRepository() {
        when(mockAddressBookRepository.findByFullName(anyString())).thenReturn(Optional.empty());

        Throwable caughtException = catchThrowable(() -> addressBookService.ageDifferenceInDays("not found", "not found"));

        assertThat(caughtException).isExactlyInstanceOf(IllegalArgumentException.class);
        assertThat(caughtException).hasMessage("firstPersonFullName not found");
    }
}