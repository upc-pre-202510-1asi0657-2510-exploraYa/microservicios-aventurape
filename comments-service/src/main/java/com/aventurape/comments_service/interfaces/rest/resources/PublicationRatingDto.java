package com.aventurape.comments_service.interfaces.rest.resources;

/**
 * Recurso para representar el rating de una publicación
 */
public record PublicationRatingDto(
        Long publicationId,
        Long commentCount,
        Double averageRating,
        Integer minRating,
        Integer maxRating
) {
} 