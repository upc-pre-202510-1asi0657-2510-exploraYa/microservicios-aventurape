package com.aventurape.comments_service.domain.model.commands;

public record CreateCommentCommand(
        Long publicationId,
        Long userId,
        String content,
        Integer rating
) {
} 