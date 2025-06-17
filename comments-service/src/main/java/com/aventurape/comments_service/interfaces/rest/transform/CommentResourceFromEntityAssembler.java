package com.aventurape.comments_service.interfaces.rest.transform;

import com.aventurape.comments_service.domain.model.aggregates.Comment;
import com.aventurape.comments_service.interfaces.rest.resources.CommentResource;

public class CommentResourceFromEntityAssembler {
    
    public static CommentResource toResourceFromEntity(Comment entity) {
        return new CommentResource(
                entity.getId(),
                entity.getPublicationId(),
                entity.getUserId(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
} 