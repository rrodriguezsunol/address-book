package gumtree.addressbook.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public List<Contact> findOldestPeople() {
        List<Contact> allContacts = addressBookRepository.findAll();

        if (allContacts.isEmpty()) {
            return allContacts;
        }

        Collections.sort(allContacts, Comparator.comparing(Contact::getDateOfBirth));

        Contact oldestContact = allContacts.get(0);

        return allContacts.stream()
                .filter(contact -> contact.getDateOfBirth().equals(oldestContact.getDateOfBirth()))
                .collect(Collectors.toList());
    }
}
