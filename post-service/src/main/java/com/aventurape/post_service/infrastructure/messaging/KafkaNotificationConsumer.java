package com.aventurape.post_service.infrastructure.messaging;

import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaNotificationConsumer {

    @KafkaListener(topics = "category-notifications", groupId = "post-service-group")
    public void listen(String message) {
        System.out.println("Received notification: " + message);
        // Aquí puedes procesar la notificación
    }
}