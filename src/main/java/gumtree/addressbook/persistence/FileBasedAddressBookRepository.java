package gumtree.addressbook.persistence;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import gumtree.addressbook.domain.Contact;
import gumtree.addressbook.domain.Gender;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import static java.lang.String.format;

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
                LocalDate dateOfBirth = mapToLocalDate(csvRecord);

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

    @Override
    public Optional<Contact> findByFullName(String fullName) {
        return contacts.stream().filter(contact -> contact.getFullName().equals(fullName)).findFirst();
    }

    private void validateNumberOfColumns(CSVRecord csvRecord) {
        if (csvRecord.size() != 3) {
            throw new PersistenceException(csvRecord.getRecordNumber(), "There are missing fields");
        }
    }

    private Gender mapToGender(CSVRecord csvRecord) {
        String trimmedGenderValue = csvRecord.get(GENDER_COLUMN_INDEX).trim();
        try {
            return Gender.valueOf(trimmedGenderValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new PersistenceException(csvRecord.getRecordNumber(),
                    format("Gender must be one of \"Male\" or \"Female\". value=\"%s\"", trimmedGenderValue));
        }
    }

    private LocalDate mapToLocalDate(CSVRecord csvRecord) {
        String trimmedDobValue = csvRecord.get(DATE_OF_BIRTH_COLUMN_INDEX).trim();
        try {
            Date parsedDate = new SimpleDateFormat("dd/MM/yy").parse(trimmedDobValue);
            return parsedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (ParseException e) {
            throw new PersistenceException(csvRecord.getRecordNumber(),
                    format("Date of birth must have the format \"dd/MM/yy\". value=\"%s\"", trimmedDobValue));
        }
    }
}
