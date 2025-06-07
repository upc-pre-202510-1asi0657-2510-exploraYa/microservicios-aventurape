package com.aventurape.profile_service.domain.model.commands;

public record UpdateProfileCommand(Long id,String name, String lastName,
                                   String birthDate, String gender,
                                   String location, String category) {
}
