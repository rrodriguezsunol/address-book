package gumtree.addressbook.persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import gumtree.addressbook.domain.Contact;

import static java.util.Comparator.comparing;

public final class InMemoryAddressBookRepository implements AddressBookRepository {

    private final List<Contact> contacts;

    InMemoryAddressBookRepository(List<Contact> contacts) {
        Objects.requireNonNull(contacts, "contacts cannot be null");
        this.contacts = new ArrayList<>(contacts);
    }

    @Override
    public List<Contact> findAll() {
        return new ArrayList<>(contacts);
    }

    @Override
    public Optional<Contact> findByFullName(String fullName) {
        Objects.requireNonNull(fullName, "fullName cannot be null");

        return contacts.stream()
                .filter(contact -> contact.getFullName().equals(fullName))
                .findFirst();
    }

    @Override
    public Optional<LocalDate> findEarliestDateOfBirth() {
        return contacts.stream()
                .sorted(comparing(Contact::getDateOfBirth))
                .findFirst()
                .map(Contact::getDateOfBirth);
    }

    @Override
    public List<Contact> findByDateOfBirth(LocalDate dateOfBirth) {
        Objects.requireNonNull(dateOfBirth, "dateOfBirth cannot be null");

        return contacts.stream()
                .filter(contact -> contact.getDateOfBirth().equals(dateOfBirth))
                .collect(Collectors.toList());
    }
}
