package com.aventurape.comments_service.interfaces.rest.resources;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public record UpdateCommentResource(
        String content,
        @Min(1) @Max(10) Integer rating
) {
} 