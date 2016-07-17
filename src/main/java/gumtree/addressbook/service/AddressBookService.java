package gumtree.addressbook.service;

import java.util.Objects;
import java.util.Optional;

import gumtree.addressbook.domain.Contact;
import gumtree.addressbook.domain.Gender;
import gumtree.addressbook.persistence.AddressBookRepository;

public final class AddressBookService {
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

    public Optional<Contact> findOldestPerson() {
        return addressBookRepository.findAll().stream()
                .sorted((left, right) -> left.getDateOfBirth().compareTo(right.getDateOfBirth()))
                .findFirst();
    }
}
