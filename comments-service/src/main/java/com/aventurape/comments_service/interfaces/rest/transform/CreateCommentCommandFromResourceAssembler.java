package com.aventurape.comments_service.interfaces.rest.transform;

import com.aventurape.comments_service.domain.model.commands.CreateCommentCommand;
import com.aventurape.comments_service.interfaces.rest.resources.CreateCommentResource;

public class CreateCommentCommandFromResourceAssembler {
    
    public static CreateCommentCommand toCommandFromResource(CreateCommentResource resource) {
        return new CreateCommentCommand(
                resource.publicationId(),
                resource.userId(),
                resource.content()
        );
    }
} 