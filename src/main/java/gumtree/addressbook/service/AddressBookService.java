package gumtree.addressbook.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import gumtree.addressbook.domain.Contact;
import gumtree.addressbook.domain.Gender;
import gumtree.addressbook.persistence.AddressBookRepository;

public final class AddressBookService {
    private final AddressBookRepository addressBookRepository;

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
        Optional<LocalDate> earliestDateOfBirth = addressBookRepository.findEarliestDateOfBirth();

        return earliestDateOfBirth
                .map(addressBookRepository::findByDateOfBirth)
                .orElse(Collections.emptyList());
    }

    public long ageDifferenceInDays(String firstPersonFullName, String secondPersonFullName) {
        Contact firstPerson = addressBookRepository.findByFullName(firstPersonFullName)
                .orElseThrow(() -> new IllegalArgumentException("firstPersonFullName not found"));

        Contact secondPerson = addressBookRepository.findByFullName(secondPersonFullName)
                .orElseThrow(() -> new IllegalArgumentException("secondPersonFullName not found"));

        return Math.abs(ChronoUnit.DAYS.between(firstPerson.getDateOfBirth(), secondPerson.getDateOfBirth()));
    }
}
