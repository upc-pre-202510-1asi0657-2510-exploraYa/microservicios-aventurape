package com.aventurape.post_service.domain.model.commands;

public record CreatePublicationCommand(
        Long entrepreneurId,
        String image,
        Integer cost,
        String nameActivity,
        String description,
        Integer timeDuration,
        Integer cantPeople
) {} 