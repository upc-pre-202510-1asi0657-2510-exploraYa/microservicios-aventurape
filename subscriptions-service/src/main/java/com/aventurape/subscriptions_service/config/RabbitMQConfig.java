package com.aventurape.subscriptions_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue categoryNotificationsQueue() {
        return new Queue("category-notifications", true);
    }
} 