package com.aventurape.comments_service.application.internal;

import com.aventurape.comments_service.domain.model.aggregates.Comment;
import com.aventurape.comments_service.domain.model.queries.GetAllCommentsQuery;
import com.aventurape.comments_service.domain.model.queries.GetCommentByIdQuery;
import com.aventurape.comments_service.domain.model.queries.GetCommentsByPublicationIdQuery;
import com.aventurape.comments_service.domain.model.queries.GetCommentsByUserIdQuery;
import com.aventurape.comments_service.domain.services.CommentQueryService;
import com.aventurape.comments_service.infrastructure.persistence.jpa.repositories.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class QueryServiceImpl implements CommentQueryService {

    private static final Logger logger = LoggerFactory.getLogger(QueryServiceImpl.class);
    private final CommentRepository commentRepository;

    public QueryServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> handle(GetAllCommentsQuery query) {
        logger.info("Getting all comments");
        return commentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> handle(GetCommentByIdQuery query) {
        logger.info("Getting comment with ID: {}", query.id());
        return commentRepository.findById(query.id());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> handle(GetCommentsByPublicationIdQuery query) {
        logger.info("Getting comments for publication ID: {}", query.publicationId());
        return commentRepository.findByPublicationId(query.publicationId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> handle(GetCommentsByUserIdQuery query) {
        logger.info("Getting comments for user ID: {}", query.userId());
        return commentRepository.findByUserId(query.userId());
    }
} 