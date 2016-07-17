package gumtree.addressbook.persistence;


import java.util.List;
import java.util.Optional;

import gumtree.addressbook.domain.Contact;

public interface AddressBookRepository {

    List<Contact> findAll();

    Optional<Contact> findByFullName(String fullName);
}
