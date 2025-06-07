package com.aventurape.profile_service.interfaces.rest.resources;

public record ProfileEntrepreneurResource(
        Long id,
        String name,
        String city,
        String country,
        String number,
        String postalCode,
        String streetAddress,
        String emailAddress
) {
}
