package gumtree.addressbook.persistence;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import gumtree.addressbook.domain.Contact;
import gumtree.addressbook.domain.Gender;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class FileBasedAddressBookRepository implements AddressBookRepository {
    private List<Contact> contacts;

    public FileBasedAddressBookRepository(String addressBookFilePath) {
        Objects.requireNonNull(addressBookFilePath, "addressBookFilePath cannot be null");

        try {
            URL addressBookResourceUri = getClass().getClassLoader().getResource(addressBookFilePath);

            if (addressBookResourceUri == null) {
                throw new IllegalArgumentException("Address book file does not exist");
            }

            CSVParser parse = CSVParser.parse(addressBookResourceUri, Charset.defaultCharset(), CSVFormat.DEFAULT);
            contacts = new ArrayList<>();
            for (CSVRecord csvRecord : parse) {
                contacts.add(new Contact(
                        csvRecord.get(0),
                        Gender.valueOf(csvRecord.get(1).toUpperCase().trim()),
                        csvRecord.get(2).trim()));
            }
        } catch (IOException e) {
            // Todo: handle this exception properly
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Contact> findAll() {
        return new ArrayList<>(contacts);
    }
}
