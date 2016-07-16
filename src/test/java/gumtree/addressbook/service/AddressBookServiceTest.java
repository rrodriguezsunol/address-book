package gumtree.addressbook.service;

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
        List<Contact> singleFemaleContactList = singletonList(new Contact("Sara Stone", Gender.FEMALE, "01/01/89"));
        when(mockAddressBookRepository.findAll()).thenReturn(singleFemaleContactList);

        int actualCount = addressBookService.countByGender(Gender.FEMALE);

        assertThat(actualCount).isEqualTo(1);
    }

    @Test
    public void countMalesReturnsOneWhenRepositoryReturnsListWithOneMale() {
        List<Contact> singleFemaleContactList = singletonList(new Contact("John Doe", Gender.MALE, "01/01/89"));
        when(mockAddressBookRepository.findAll()).thenReturn(singleFemaleContactList);

        int actualCount = addressBookService.countByGender(Gender.MALE);

        assertThat(actualCount).isEqualTo(1);
    }

    @Test
    public void countFemalesReturnsFemaleCountWhenRepositoryReturnsListWithMixedGenders() {
        List<Contact> mixedGenderContactList = asList(
                new Contact("Sara Stone", Gender.FEMALE, "01/01/89"),
                new Contact("John Doe", Gender.MALE, "01/01/89"),
                new Contact("Gemma Lane", Gender.FEMALE, "01/01/89"));
        when(mockAddressBookRepository.findAll()).thenReturn(mixedGenderContactList);

        int actualCount = addressBookService.countByGender(Gender.FEMALE);

        assertThat(actualCount).isEqualTo(2);
    }

    @Test
    public void countMalesReturnsMaleCountWhenRepositoryReturnsListWithMixedGenders() {
        List<Contact> mixedGenderContactList = asList(
                new Contact("Tom Ford", Gender.MALE, "01/01/89"),
                new Contact("Sara Stone", Gender.FEMALE, "01/01/89"),
                new Contact("John Doe", Gender.MALE, "01/01/89"));
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
}