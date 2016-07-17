package gumtree.addressbook.domain;

import java.time.LocalDate;
import java.util.Objects;

import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public final class Contact implements Comparable<Contact> {
    private String fullName;
    private Gender gender;
    private LocalDate dateOfBirth;

    public Contact(String fullName, Gender gender, LocalDate dateOfBirth) {
        this.fullName = fullName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    public String getFullName() {
        return fullName;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Contact contact = (Contact) other;
        return Objects.equals(fullName, contact.fullName) &&
                Objects.equals(gender, contact.gender) &&
                Objects.equals(dateOfBirth, contact.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, gender, dateOfBirth);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("fullName", fullName)
                .append("gender", gender)
                .append("dateOfBirth", dateOfBirth)
                .toString();
    }

    @Override
    public int compareTo(@NotNull Contact other) {
        return fullName.compareTo(other.fullName);
    }
}
