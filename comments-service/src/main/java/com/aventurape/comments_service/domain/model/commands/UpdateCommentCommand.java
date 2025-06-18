package com.aventurape.comments_service.domain.model.commands;

public record UpdateCommentCommand(
        Long id,
        String content,
        Integer rating
) {
} 