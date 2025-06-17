package com.aventurape.favorites_service.interfaces.rest.resources;

public record FavoriteResource(
        Long id,
        Long profileId,
        Long publicationId) {
}