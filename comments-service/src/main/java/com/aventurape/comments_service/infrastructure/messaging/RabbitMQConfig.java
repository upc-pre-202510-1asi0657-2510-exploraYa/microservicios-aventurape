package com.aventurape.comments_service.infrastructure.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String COMMENT_EXCHANGE = "comment.exchange";
    public static final String COMMENT_CREATED_QUEUE = "comment.created.queue";
    public static final String COMMENT_UPDATED_QUEUE = "comment.updated.queue";
    public static final String COMMENT_DELETED_QUEUE = "comment.deleted.queue";
    public static final String COMMENT_CREATED_ROUTING_KEY = "comment.created";
    public static final String COMMENT_UPDATED_ROUTING_KEY = "comment.updated";
    public static final String COMMENT_DELETED_ROUTING_KEY = "comment.deleted";

    @Bean
    public Exchange commentExchange() {
        return ExchangeBuilder.topicExchange(COMMENT_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue commentCreatedQueue() {
        return QueueBuilder.durable(COMMENT_CREATED_QUEUE).build();
    }

    @Bean
    public Queue commentUpdatedQueue() {
        return QueueBuilder.durable(COMMENT_UPDATED_QUEUE).build();
    }

    @Bean
    public Queue commentDeletedQueue() {
        return QueueBuilder.durable(COMMENT_DELETED_QUEUE).build();
    }

    @Bean
    public Binding commentCreatedBinding() {
        return BindingBuilder
                .bind(commentCreatedQueue())
                .to(commentExchange())
                .with(COMMENT_CREATED_ROUTING_KEY)
                .noargs();
    }

    @Bean
    public Binding commentUpdatedBinding() {
        return BindingBuilder
                .bind(commentUpdatedQueue())
                .to(commentExchange())
                .with(COMMENT_UPDATED_ROUTING_KEY)
                .noargs();
    }

    @Bean
    public Binding commentDeletedBinding() {
        return BindingBuilder
                .bind(commentDeletedQueue())
                .to(commentExchange())
                .with(COMMENT_DELETED_ROUTING_KEY)
                .noargs();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
} 