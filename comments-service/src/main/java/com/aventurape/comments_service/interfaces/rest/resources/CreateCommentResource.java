package com.aventurape.comments_service.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public record CreateCommentResource(
        @NotNull Long publicationId,
        @NotBlank String content,
        @NotNull @Min(1) @Max(10) Integer rating
) {
} 