package com.aventurape.favorites_service.domain.model.commands;


import com.aventurape.favorites_service.domain.model.valueobjects.ProfileId;
import com.aventurape.favorites_service.domain.model.valueobjects.PublicationId;

public record CreateFavoritePublicationCommand(
        ProfileId profileId, PublicationId publicationId) {
}
