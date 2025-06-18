package com.aventurape.stats_service.domain.model.valueobjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Value Object para representar estadísticas generales del sistema
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralStats {
    private Long totalPublications;
    private Long totalComments;
    private Double averageRatingGlobal;
    private Double averageCommentsPerPublication;
} 