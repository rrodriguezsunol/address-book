package gumtree.addressbook.persistence;

import java.net.URL;
import java.util.Objects;

public final class AddressBookRepositoryFactory {

    private AddressBookRepositoryFactory() {}

    public static AddressBookRepository newCsvInstance(String csvFilePathInJar) {
        Objects.requireNonNull(csvFilePathInJar, "csvFilePathInJar cannot be null");

        URL addressBookResourceUri = AddressBookRepositoryFactory.class.getClassLoader().getResource(csvFilePathInJar);

        if (addressBookResourceUri == null) {
            throw new IllegalArgumentException("Address book file does not exist");
        }

        CsvAddressBookReader csvAddressBookReader = new CsvAddressBookReader(addressBookResourceUri);

        return new InMemoryAddressBookRepository(csvAddressBookReader.read());
    }
}
