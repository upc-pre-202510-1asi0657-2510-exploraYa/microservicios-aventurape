package com.aventurape.iam_service.interfaces.rest.resources;

public record AuthenticatedUserResource(Long id, String username, String token) {
}
