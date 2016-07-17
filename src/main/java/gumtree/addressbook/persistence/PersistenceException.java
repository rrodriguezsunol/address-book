package gumtree.addressbook.persistence;

import static java.lang.String.format;

public class PersistenceException extends RuntimeException {

    public PersistenceException(Throwable cause) {
        super("Unexpected error", cause);
    }

    public PersistenceException(long lineNumber, String message) {
        super(format("Line %d is invalid. %s", lineNumber, message));
    }
}
