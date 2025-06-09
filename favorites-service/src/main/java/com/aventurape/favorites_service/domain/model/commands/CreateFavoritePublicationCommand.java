package com.aventurape.favorites_service.domain.model.commands;

import com.upc.aventurape.platform.favorite.domain.model.valueobjects.ProfileId;
import com.upc.aventurape.platform.favorite.domain.model.valueobjects.PublicationId;

public record CreateFavoritePublicationCommand(
        ProfileId profileId, PublicationId publicationId) {
}
