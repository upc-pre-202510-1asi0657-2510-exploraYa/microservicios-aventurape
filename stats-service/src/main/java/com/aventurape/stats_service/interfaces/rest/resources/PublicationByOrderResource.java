package com.aventurape.stats_service.interfaces.rest.resources;

/**
 * DTO para representar una publicación ordenada por rating
 */
public record PublicationByOrderResource(
       Long id,
       Long entrepreneurId,
       String nameActivity,
       String description,
       Integer timeDuration,
       String image,
       Integer cantPeople,
       Integer cost,
       Double averageRating
) {} 