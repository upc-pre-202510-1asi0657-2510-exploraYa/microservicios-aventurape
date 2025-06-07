package com.aventurape.post_service.interfaces.rest.resources;

public record CreatePublicationResource(
        Long entrepreneurId,
        String image,
        Integer cost,
        String nameActivity,
        String description,
        Integer timeDuration,
        Integer cantPeople
) {} 