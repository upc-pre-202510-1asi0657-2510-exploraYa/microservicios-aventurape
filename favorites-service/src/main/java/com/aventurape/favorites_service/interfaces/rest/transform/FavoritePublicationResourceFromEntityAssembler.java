package com.aventurape.favorites_service.interfaces.rest.transform;

import com.upc.aventurape.platform.favorite.domain.model.aggregates.Favorite;
import com.upc.aventurape.platform.favorite.interfaces.rest.resources.FavoriteResource;

public class FavoritePublicationResourceFromEntityAssembler {
    public static FavoriteResource toResourceFromEntity(Favorite entity) {
        return new FavoriteResource(
                entity.getId(),
                entity.getProfileId().profileId(),
                entity.getPublicationId().getId()
        );
    }
}