package com.aventurape.comments_service.interfaces.rest.resources;

public record CreateCommentResource(
        Long publicationId,
        Long userId,
        String content
) {
} 