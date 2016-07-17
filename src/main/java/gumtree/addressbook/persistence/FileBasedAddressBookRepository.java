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
    private static final int FULL_NAME_COLUMN_INDEX = 0;
    private static final int GENDER_COLUMN_INDEX = 1;
    private static final int DATE_OF_BIRTH_COLUMN_INDEX = 2;

    private List<Contact> contacts;

    public FileBasedAddressBookRepository(String addressBookFilePath) {
        Objects.requireNonNull(addressBookFilePath, "addressBookFilePath cannot be null");

        try {
            URL addressBookResourceUri = getClass().getClassLoader().getResource(addressBookFilePath);

            if (addressBookResourceUri == null) {
                throw new IllegalArgumentException("Address book file does not exist");
            }

            CSVParser parse = CSVParser.parse(addressBookResourceUri, Charset.forName("UTF-8"), CSVFormat.DEFAULT);
            contacts = new ArrayList<>();
            for (CSVRecord csvRecord : parse) {
                validateNumberOfColumns(csvRecord);

                String fullName = csvRecord.get(FULL_NAME_COLUMN_INDEX);
                Gender gender = mapToGender(csvRecord);
                String dateOfBirth = csvRecord.get(DATE_OF_BIRTH_COLUMN_INDEX).trim();

                contacts.add(new Contact(fullName, gender, dateOfBirth));
            }
        } catch (IOException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<Contact> findAll() {
        return new ArrayList<>(contacts);
    }


    private void validateNumberOfColumns(CSVRecord csvRecord) {
        if (csvRecord.size() != 3) {
            throw new PersistenceException(csvRecord.getRecordNumber(), "There are missing fields");
        }
    }

    private Gender mapToGender(CSVRecord csvRecord) {
        try {
            return Gender.valueOf(csvRecord.get(GENDER_COLUMN_INDEX).toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            throw new PersistenceException(csvRecord.getRecordNumber(),
                    "Gender must be one of \"Male\" or \"Female\". value=\"invalid gender\"");
        }
    }
}
