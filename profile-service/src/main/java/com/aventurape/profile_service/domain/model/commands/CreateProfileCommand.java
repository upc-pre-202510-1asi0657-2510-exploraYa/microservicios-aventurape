package com.aventurape.profile_service.domain.model.commands;

public record CreateProfileCommand(String name, String lastName,
                                   String birthDate, String gender,
                                   String location, String category) {
}