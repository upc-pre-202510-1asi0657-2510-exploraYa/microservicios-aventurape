package com.aventurape.post_service.interfaces.rest.resources;

public record PublicationResource(
        Long id,
        Long entrepreneurId,
        String nameActivity,
        String description,
        Integer timeDuration,
        String image,
        Integer cantPeople,
        Integer cost
) {} 