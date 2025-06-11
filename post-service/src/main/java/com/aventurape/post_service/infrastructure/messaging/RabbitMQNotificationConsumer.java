package com.aventurape.post_service.infrastructure.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQNotificationConsumer {

    @RabbitListener(queues = "category-notifications")
    public void receiveMessage(String message) {
        System.out.println("Received notification: " + message);
        // Aquí puedes procesar la notificación
    }
}