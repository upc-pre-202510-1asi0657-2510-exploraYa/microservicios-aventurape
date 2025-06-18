package com.aventurape.stats_service.domain.model.valueobjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Value Object para representar las estadísticas de una publicación
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicationStats {
    private Long publicationId;
    private Long commentCount;
    private Double averageRating;
    private Integer minRating;
    private Integer maxRating;
} 