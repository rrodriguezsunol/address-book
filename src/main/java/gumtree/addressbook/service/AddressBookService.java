package gumtree.addressbook.service;

import java.util.Objects;

import gumtree.addressbook.domain.Gender;
import gumtree.addressbook.persistence.AddressBookRepository;

public class AddressBookService {
    private AddressBookRepository addressBookRepository;

    public AddressBookService(AddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    public int countByGender(Gender gender) {
        Objects.requireNonNull(gender, "gender cannot be null");

        return (int) addressBookRepository.findAll().stream()
                .filter(contact -> contact.getGender().equals(gender))
                .count();
    }
}
