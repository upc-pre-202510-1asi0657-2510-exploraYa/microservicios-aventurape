package com.aventurape.profile_service.interfaces.rest.resources;


public record CreateProfileEntrepreneurResource(
        String nameEntrepreneurship,
        String addressCity,
        String addressCountry,
        String addressNumber,
        String addressPostalCode,
        String addressStreet,
        String emailAddress
) {
    public CreateProfileEntrepreneurResource {
        if (nameEntrepreneurship == null || nameEntrepreneurship.isBlank()) {
            throw new IllegalArgumentException("NameEntrepreneurship must not be null or blank");
        }
        if (addressCity == null || addressCity.isBlank()) {
            throw new IllegalArgumentException("AddressCity must not be null or blank");
        }
        if (addressCountry == null || addressCountry.isBlank()) {
            throw new IllegalArgumentException("AddressCountry must not be null or blank");
        }
        if (addressNumber == null || addressNumber.isBlank()) {
            throw new IllegalArgumentException("AddressNumber must not be null or blank");
        }
        if (addressPostalCode == null || addressPostalCode.isBlank()) {
            throw new IllegalArgumentException("AddressPostalCode must not be null or blank");
        }
        if (addressStreet == null || addressStreet.isBlank()) {
            throw new IllegalArgumentException("AddressStreet must not be null or blank");
        }
        if (emailAddress == null || emailAddress.isBlank()) {
            throw new IllegalArgumentException("EmailAddress must not be null or blank");
        }
    }
}