package com.aventurape.stats_service.domain.model.queries;

import com.aventurape.stats_service.domain.model.valueobjects.EntrepreneurId;

public record GetFavoritePublicationsByProfileIdOrderedByRatingQuery(
        Long entrepeneurId
) {
    public EntrepreneurId entrepreneurId() {
        return new EntrepreneurId(entrepeneurId);
    }
} 