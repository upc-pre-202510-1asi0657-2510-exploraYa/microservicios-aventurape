package com.aventurape.favorites_service.interfaces.rest.transform;

import com.aventurape.favorites_service.domain.model.aggregates.Favorite;
import com.aventurape.favorites_service.interfaces.rest.resources.FavoriteResource;

public class FavoritePublicationResourceFromEntityAssembler {
    public static FavoriteResource toResourceFromEntity(Favorite entity) {
        return new FavoriteResource(
                entity.getId(),
                entity.getProfileId().profileId(),
                entity.getPublicationId().getId()
        );
    }
}