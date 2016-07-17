package gumtree.addressbook;

import gumtree.addressbook.domain.Contact;
import gumtree.addressbook.domain.Gender;
import gumtree.addressbook.persistence.AddressBookRepository;
import gumtree.addressbook.persistence.FileBasedAddressBookRepository;
import gumtree.addressbook.service.AddressBookService;

public class AddressBookApp {
    private AddressBookService addressBookService;

    public AddressBookApp() {
        AddressBookRepository addressBookRepository = new FileBasedAddressBookRepository("AddressBook");
        addressBookService = new AddressBookService(addressBookRepository);
    }

    public int countNumberOfMales() {
        return addressBookService.countByGender(Gender.MALE);
    }

    public String getOldestPerson() {
        return addressBookService.findOldestPerson().map(Contact::getFullName).orElse("");
    }
}
