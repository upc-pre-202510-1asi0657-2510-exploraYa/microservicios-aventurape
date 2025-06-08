package com.aventurape.profile_service.domain.model.commands;

public record CreateProfileEntrepreneurCommand(
        Long userId,
        String email,
        String street,
        String number,
        String city,
        String postalCode,
        String country,
        String name
) {
    public CreateProfileEntrepreneurCommand {
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
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be null or blank");
        }
    }
}