package gumtree.addressbook;

import java.util.List;

import gumtree.addressbook.domain.Contact;
import gumtree.addressbook.domain.Gender;
import gumtree.addressbook.service.AddressBookService;

import static java.util.stream.Collectors.toList;

public class AddressBookApp {
    private final AddressBookService addressBookService;

    public AddressBookApp(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    public int countNumberOfMales() {
        return addressBookService.countByGender(Gender.MALE);
    }

    public List<String> getOldestPerson() {
        return addressBookService.findOldestPeople().stream().map(Contact::getFullName).collect(toList());
    }

    public long ageDifferenceInDaysBetweenBillAndPaul() {
        return addressBookService.ageDifferenceInDays("Bill McKnight", "Paul Robinson");
    }

    public static void main(String... args) {
        AddressBookApp addressBookApp = AddressBookAppFactory.newInstance("AddressBook");

        System.out.printf("Number of males in the address book: %d\n", addressBookApp.countNumberOfMales());
        System.out.printf("Oldest person in the address book is: %s\n", addressBookApp.getOldestPerson());
        System.out.printf("Bill is %d days older than Paul\n", addressBookApp.ageDifferenceInDaysBetweenBillAndPaul());
    }
}
