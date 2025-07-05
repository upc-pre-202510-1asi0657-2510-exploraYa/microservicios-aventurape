package com.aventurape.comments_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=password",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "eureka.client.enabled=false",
    "spring.rabbitmq.host=localhost",
    "spring.rabbitmq.port=5672",
    "spring.rabbitmq.username=guest",
    "spring.rabbitmq.password=guest",
    "spring.cloud.config.enabled=false",
    "authorization.jwt.secret=AventuraPeSecureTokenKey2025WithStrongSecurityRequiredFor256Bits",
    "authorization.jwt.expiration.days=7",
    "post-service.ribbon.listOfServers=localhost:8083",
    "documentation.application.description=Test Description",
    "documentation.application.version=0.0.1-SNAPSHOT"
})
class CommentsServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
