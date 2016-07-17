package gumtree.addressbook.persistence;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class AddressBookRepositoryFactoryTest {

    @Test
    public void newCsvInstanceThrowsExceptionWhenAddressBookPathDoesNotExist() {

        Throwable caughtException = catchThrowable(() -> AddressBookRepositoryFactory.newCsvInstance("does not exist"));

        assertThat(caughtException).isExactlyInstanceOf(IllegalArgumentException.class);
        assertThat(caughtException.getMessage()).isEqualTo("Address book file does not exist");
    }

    @Test
    public void newCsvInstanceThrowsExceptionWhenAddressBookPathIsNull() {

        Throwable caughtException = catchThrowable(() -> AddressBookRepositoryFactory.newCsvInstance(null));

        assertThat(caughtException).isExactlyInstanceOf(NullPointerException.class);
        assertThat(caughtException.getMessage()).isEqualTo("csvFilePathInJar cannot be null");
    }
}