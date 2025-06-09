package com.aventurape.subscriptions_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
public class SubscriptionsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriptionsServiceApplication.class, args);
	}

}
