package com.aventurape.stats_service.interfaces.rest.transform;

import com.aventurape.stats_service.domain.model.valueobjects.GeneralStats;
import com.aventurape.stats_service.domain.model.valueobjects.PublicationStats;
import com.aventurape.stats_service.interfaces.rest.resources.GeneralStatsResource;
import com.aventurape.stats_service.interfaces.rest.resources.PublicationStatsResource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Transformador para convertir entre objetos de dominio y recursos REST
 */
public class StatsResourceFromEntityAssembler {
    
    /**
     * Convierte un objeto PublicationStats a PublicationStatsResource
     */
    public static PublicationStatsResource toResourceFromEntity(PublicationStats entity) {
        return new PublicationStatsResource(
                entity.getPublicationId(),
                entity.getCommentCount(),
                entity.getAverageRating(),
                entity.getMinRating(),
                entity.getMaxRating()
        );
    }
    
    /**
     * Convierte un objeto GeneralStats a GeneralStatsResource
     */
    public static GeneralStatsResource toResourceFromEntity(GeneralStats entity) {
        return new GeneralStatsResource(
                entity.getTotalPublications(),
                entity.getTotalComments(),
                entity.getAverageRatingGlobal(),
                entity.getAverageCommentsPerPublication()
        );
    }
    
    /**
     * Convierte una lista de PublicationStats a lista de PublicationStatsResource
     */
    public static List<PublicationStatsResource> toResourceFromEntityList(List<PublicationStats> entityList) {
        return entityList.stream()
                .map(StatsResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
    }
} 