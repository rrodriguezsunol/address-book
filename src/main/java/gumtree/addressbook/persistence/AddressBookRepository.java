package gumtree.addressbook.persistence;


import java.util.List;

import gumtree.addressbook.domain.Contact;

public interface AddressBookRepository {

    List<Contact> findAll();
}
