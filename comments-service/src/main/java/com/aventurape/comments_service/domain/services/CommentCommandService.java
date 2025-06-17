package com.aventurape.comments_service.domain.services;

import com.aventurape.comments_service.domain.model.aggregates.Comment;
import com.aventurape.comments_service.domain.model.commands.CreateCommentCommand;
import com.aventurape.comments_service.domain.model.commands.UpdateCommentCommand;

import java.util.Optional;

public interface CommentCommandService {
    Optional<Comment> handle(CreateCommentCommand command);
    Optional<Comment> handle(UpdateCommentCommand command);
    boolean handle(Long commentId);
} 