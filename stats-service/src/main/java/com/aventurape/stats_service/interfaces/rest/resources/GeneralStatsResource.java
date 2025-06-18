package com.aventurape.stats_service.interfaces.rest.resources;

/**
 * Recurso para representar estadísticas generales en respuestas REST
 */
public record GeneralStatsResource(
        Long totalPublications,
        Long totalComments,
        Double averageRatingGlobal,
        Double averageCommentsPerPublication
) {
} 