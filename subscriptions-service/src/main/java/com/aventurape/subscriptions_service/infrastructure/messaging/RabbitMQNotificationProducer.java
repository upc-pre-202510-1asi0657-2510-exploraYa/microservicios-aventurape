package com.aventurape.subscriptions_service.infrastructure.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQNotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQNotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendNotification(String routingKey, String message) {
        rabbitTemplate.convertAndSend(routingKey, message);
    }
}