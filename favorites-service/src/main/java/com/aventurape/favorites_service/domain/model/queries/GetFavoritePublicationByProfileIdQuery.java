package com.aventurape.favorites_service.domain.model.queries;

import com.upc.aventurape.platform.favorite.domain.model.valueobjects.ProfileId;

public record GetFavoritePublicationByProfileIdQuery(ProfileId profileId) {
}