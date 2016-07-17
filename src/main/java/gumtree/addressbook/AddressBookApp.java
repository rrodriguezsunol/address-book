package gumtree.addressbook;

import java.util.List;

import gumtree.addressbook.domain.Contact;
import gumtree.addressbook.domain.Gender;
import gumtree.addressbook.persistence.AddressBookRepository;
import gumtree.addressbook.persistence.FileBasedAddressBookRepository;
import gumtree.addressbook.service.AddressBookService;

import static java.util.stream.Collectors.toList;

public class AddressBookApp {
    private AddressBookService addressBookService;

    public AddressBookApp() {
        AddressBookRepository addressBookRepository = new FileBasedAddressBookRepository("AddressBook");
        addressBookService = new AddressBookService(addressBookRepository);
    }

    public int countNumberOfMales() {
        return addressBookService.countByGender(Gender.MALE);
    }

    public List<String> getOldestPerson() {
        return addressBookService.findOldestPeople().stream().map(Contact::getFullName).collect(toList());
    }

    public long ageDifferenceInDaysBetweenBillAndPaul() {
        return addressBookService.ageDifferenceInDays("Bill McKnight", "Paul Robinson");
    }
}
