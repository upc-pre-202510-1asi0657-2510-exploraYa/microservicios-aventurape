package com.aventurape.stats_service.interfaces.rest.clients;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar el rating de una publicación
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicationRatingDto {
    private Long publicationId;
    private Long commentCount;
    private Double averageRating;
    private Integer minRating;
    private Integer maxRating;
} 