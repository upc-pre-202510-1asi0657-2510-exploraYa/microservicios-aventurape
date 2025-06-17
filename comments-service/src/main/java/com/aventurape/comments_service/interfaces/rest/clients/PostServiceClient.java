package com.aventurape.comments_service.interfaces.rest.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mscv-post", url = "${post-service.ribbon.listOfServers}")
public interface PostServiceClient {

    @GetMapping("/api/v1/publications/{publicationId}")
    ResponseEntity<Object> getPublicationById(@PathVariable("publicationId") Long publicationId);

    @GetMapping("/api/v1/publications/exists/{publicationId}")
    ResponseEntity<Boolean> existsPublicationById(@PathVariable("publicationId") Long publicationId);
} 