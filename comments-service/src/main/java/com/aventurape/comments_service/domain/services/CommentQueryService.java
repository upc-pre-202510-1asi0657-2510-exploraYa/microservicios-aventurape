package com.aventurape.comments_service.domain.services;

import com.aventurape.comments_service.domain.model.aggregates.Comment;
import com.aventurape.comments_service.domain.model.queries.GetAllCommentsQuery;
import com.aventurape.comments_service.domain.model.queries.GetCommentByIdQuery;
import com.aventurape.comments_service.domain.model.queries.GetCommentsByPublicationIdQuery;
import com.aventurape.comments_service.domain.model.queries.GetCommentsByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface CommentQueryService {
    List<Comment> handle(GetAllCommentsQuery query);
    Optional<Comment> handle(GetCommentByIdQuery query);
    List<Comment> handle(GetCommentsByPublicationIdQuery query);
    List<Comment> handle(GetCommentsByUserIdQuery query);
} 