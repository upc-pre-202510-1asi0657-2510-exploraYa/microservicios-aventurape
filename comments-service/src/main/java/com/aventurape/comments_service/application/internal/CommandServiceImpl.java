package com.aventurape.comments_service.application.internal;

import com.aventurape.comments_service.domain.model.aggregates.Comment;
import com.aventurape.comments_service.domain.model.commands.CreateCommentCommand;
import com.aventurape.comments_service.domain.model.commands.UpdateCommentCommand;
import com.aventurape.comments_service.domain.services.CommentCommandService;
import com.aventurape.comments_service.infrastructure.messaging.RabbitMQCommentEventPublisher;
import com.aventurape.comments_service.infrastructure.persistence.jpa.repositories.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CommandServiceImpl implements CommentCommandService {

    private static final Logger logger = LoggerFactory.getLogger(CommandServiceImpl.class);
    private final CommentRepository commentRepository;
    private final RabbitMQCommentEventPublisher eventPublisher;

    public CommandServiceImpl(CommentRepository commentRepository, RabbitMQCommentEventPublisher eventPublisher) {
        this.commentRepository = commentRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Optional<Comment> handle(CreateCommentCommand command) {
        logger.info("Creating comment for publication ID: {}, user ID: {}", command.publicationId(), command.userId());
        
        var comment = new Comment(command.publicationId(), command.userId(), command.content());
        var savedComment = commentRepository.save(comment);
        
        // Publicar evento de comentario creado
        eventPublisher.publishCommentCreated(savedComment);
        
        return Optional.of(savedComment);
    }

    @Override
    @Transactional
    public Optional<Comment> handle(UpdateCommentCommand command) {
        logger.info("Updating comment with ID: {}", command.id());
        
        var commentOptional = commentRepository.findById(command.id());
        if (commentOptional.isEmpty()) {
            logger.warn("Comment with ID {} not found", command.id());
            return Optional.empty();
        }
        
        var comment = commentOptional.get();
        comment.updateContent(command.content());
        var updatedComment = commentRepository.save(comment);
        
        // Publicar evento de comentario actualizado
        eventPublisher.publishCommentUpdated(updatedComment);
        
        return Optional.of(updatedComment);
    }

    @Override
    @Transactional
    public boolean handle(Long commentId) {
        logger.info("Deleting comment with ID: {}", commentId);
        
        if (!commentRepository.existsById(commentId)) {
            logger.warn("Comment with ID {} not found", commentId);
            return false;
        }
        
        // Publicar evento de comentario eliminado antes de eliminarlo
        var commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            eventPublisher.publishCommentDeleted(commentOptional.get());
        }
        
        commentRepository.deleteById(commentId);
        return true;
    }
} 