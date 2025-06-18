package com.aventurape.stats_service.interfaces.rest.resources;

/**
 * Recurso para representar estadísticas de una publicación en respuestas REST
 */
public record PublicationStatsResource(
        Long publicationId,
        Long commentCount,
        Double averageRating,
        Integer minRating,
        Integer maxRating
) {
} 