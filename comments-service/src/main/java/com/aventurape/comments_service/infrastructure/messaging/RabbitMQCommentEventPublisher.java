package com.aventurape.comments_service.infrastructure.messaging;

import com.aventurape.comments_service.domain.model.aggregates.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQCommentEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQCommentEventPublisher.class);
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQCommentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCommentCreated(Comment comment) {
        logger.info("Publishing comment created event for comment ID: {}", comment.getId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.COMMENT_EXCHANGE,
                RabbitMQConfig.COMMENT_CREATED_ROUTING_KEY,
                comment
        );
    }

    public void publishCommentUpdated(Comment comment) {
        logger.info("Publishing comment updated event for comment ID: {}", comment.getId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.COMMENT_EXCHANGE,
                RabbitMQConfig.COMMENT_UPDATED_ROUTING_KEY,
                comment
        );
    }

    public void publishCommentDeleted(Comment comment) {
        logger.info("Publishing comment deleted event for comment ID: {}", comment.getId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.COMMENT_EXCHANGE,
                RabbitMQConfig.COMMENT_DELETED_ROUTING_KEY,
                comment
        );
    }
} 