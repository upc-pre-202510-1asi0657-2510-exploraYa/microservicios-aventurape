package com.aventurape.comments_service.interfaces.rest.resources;

import java.util.Date;

public record CommentResource(
        Long id,
        Long publicationId,
        Long userId,
        String content,
        Integer rating,
        Date createdAt,
        Date updatedAt
) {
} 