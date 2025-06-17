package com.aventurape.comments_service.interfaces.rest.transform;

import com.aventurape.comments_service.domain.model.commands.UpdateCommentCommand;
import com.aventurape.comments_service.interfaces.rest.resources.UpdateCommentResource;

public class UpdateCommentCommandFromResourceAssembler {
    
    public static UpdateCommentCommand toCommandFromResource(Long commentId, UpdateCommentResource resource) {
        return new UpdateCommentCommand(
                commentId,
                resource.content()
        );
    }
} 