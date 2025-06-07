package com.aventurape.post_service.interfaces.rest.resources;

public record UpdatePublicationResource(
        String nameActivity,
        String description,
        Integer timeDuration,
        String image,
        Integer cantPeople,
        Integer cost
) {} 