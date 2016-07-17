package gumtree.addressbook;

import gumtree.addressbook.persistence.AddressBookRepository;
import gumtree.addressbook.persistence.FileBasedAddressBookRepository;
import gumtree.addressbook.service.AddressBookService;

public class AddressBookAppFactory {

    private AddressBookAppFactory() {}

    public static AddressBookApp newInstance(String addressBookFilePath) {
        AddressBookRepository addressBookRepository = new FileBasedAddressBookRepository(addressBookFilePath);

        AddressBookService addressBookService = new AddressBookService(addressBookRepository);

        return new AddressBookApp(addressBookService);
    }
}
