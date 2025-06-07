package com.aventurape.profile_service.interfaces.rest.resources;

public record ProfileAdventurerResource (
        Long id,
        String fullName,
        String gender,
        String email,
        String streetAddress
){ }
