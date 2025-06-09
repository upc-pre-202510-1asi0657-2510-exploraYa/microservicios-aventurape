package com.aventurape.profile_service.domain.model.aggregates;

import com.aventurape.profile_service.domain.model.valueobjects.EmailAddress;
import com.aventurape.profile_service.domain.model.valueobjects.StreetAddress;
import com.aventurape.profile_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import jakarta.persistence.*;

@Entity
@Table(name = "profiles") // Explicitly specify table name
@Inheritance(strategy = InheritanceType.JOINED)
public class Profile extends AuditableAbstractAggregateRoot<Profile> {

    // This class represents a profile in the system, which includes an email address and a street address.
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "email_address"))
    })
    private EmailAddress email;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "address_street")),
            @AttributeOverride(name = "number", column = @Column(name = "address_number")),
            @AttributeOverride(name = "city", column = @Column(name = "address_city")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "address_postal_code")),
            @AttributeOverride(name = "country", column = @Column(name = "address_country"))
    })
    private StreetAddress address;

    public Profile(String email, String street, String number, String city, String postalCode, String country) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be null or blank");
        }
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Street must not be null or blank");
        }
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("Number must not be null or blank");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City must not be null or blank");
        }
        if (postalCode == null || postalCode.isBlank()) {
            throw new IllegalArgumentException("PostalCode must not be null or blank");
        }
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Country must not be null or blank");
        }
        this.email = new EmailAddress(email);
        this.address = new StreetAddress(street, number, city, postalCode, country);
    }

    public Profile() { }

    public void updateEmail(String email) {
        this.email = new EmailAddress(email);
    }

    public void updateAddress(String street, String number, String city, String postalCode, String country) {
        this.address = new StreetAddress(street, number, city, postalCode, country);
    }

    public String getEmailAddress() {
        return this.email.address();
    }

    public String getStreetAddress() {
        return this.address.street();
    }
}