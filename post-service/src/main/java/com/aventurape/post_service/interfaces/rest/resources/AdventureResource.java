package com.aventurape.post_service.interfaces.rest.resources;

public record AdventureResource(
        String nameActivity,
        String description,
        Integer timeDuration,
        Integer cantPeople
) {} 