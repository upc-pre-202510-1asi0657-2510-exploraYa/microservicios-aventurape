package com.aventurape.profile_service.interfaces.rest.resources;

public record CreateProfileResource(
        String name,String lastName,
        String birthDate,String gender,
        String location,String category) {
}
