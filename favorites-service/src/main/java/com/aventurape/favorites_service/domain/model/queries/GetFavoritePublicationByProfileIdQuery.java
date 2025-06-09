package com.aventurape.favorites_service.domain.model.queries;

import com.aventurape.favorites_service.domain.model.valueobjects.ProfileId;

public record GetFavoritePublicationByProfileIdQuery(ProfileId profileId) {
}