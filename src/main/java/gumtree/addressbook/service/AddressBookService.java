package gumtree.addressbook.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.collect.TreeMultimap;
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

        TreeMultimap<LocalDate, Contact> dobOrderedMultiMap = TreeMultimap.create();

        Contact oldestContact = allContacts.stream()
                .sorted((left, right) -> left.getDateOfBirth().compareTo(right.getDateOfBirth()))
                .collect(Collectors.toList()).get(0);

        for (Contact contact : allContacts) {
            dobOrderedMultiMap.put(contact.getDateOfBirth(), contact);
        }

        return new ArrayList<>(dobOrderedMultiMap.get(oldestContact.getDateOfBirth()));
    }
}
