package gumtree.addressbook.persistence;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import gumtree.addressbook.domain.Contact;
import gumtree.addressbook.domain.Gender;

public interface AddressBookRepository {

    int countByGender(Gender gender);

    Optional<Contact> findByFullName(String fullName);

    Optional<LocalDate> findEarliestDateOfBirth();

    List<Contact> findByDateOfBirth(LocalDate dateOfBirth);
}
