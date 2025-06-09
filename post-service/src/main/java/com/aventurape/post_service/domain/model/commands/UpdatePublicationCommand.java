package com.aventurape.post_service.domain.model.commands;

public record UpdatePublicationCommand(
        Long publicationId,
        String image,
        Integer cost,
        String nameActivity,
        String description,
        Integer timeDuration,
        Integer cantPeople
) {} 