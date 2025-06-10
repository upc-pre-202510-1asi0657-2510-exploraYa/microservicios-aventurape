package com.aventurape.subscriptions_service.interfaces.rest;

import com.aventurape.subscriptions_service.domain.model.aggregates.Category;
import com.aventurape.subscriptions_service.domain.model.commands.AssignCategoryToPublicationCommand;
import com.aventurape.subscriptions_service.domain.model.commands.RemoveCategoryFromPublicationCommand;
import com.aventurape.subscriptions_service.domain.model.queries.GetCategoriesByPublicationIdQuery;
import com.aventurape.subscriptions_service.domain.model.queries.GetPublicationsByCategoryIdQuery;
import com.aventurape.subscriptions_service.domain.model.valueobjects.PublicationId;
import com.aventurape.subscriptions_service.domain.services.CategoryCommandService;
import com.aventurape.subscriptions_service.domain.services.CategoryQueryService;
import com.aventurape.subscriptions_service.infrastructure.security.jwt.JwtUserDetails;
import com.aventurape.subscriptions_service.interfaces.rest.resources.AssignCategoryRequest;
import com.aventurape.subscriptions_service.interfaces.rest.resources.CategoryResource;
import com.aventurape.subscriptions_service.interfaces.rest.transform.CategoryResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for publication-category relationships
 */
@RestController
@RequestMapping(value = "/api/v1/publication-categories", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Publication Categories")
public class PublicationCategoryController {

    private final CategoryCommandService categoryCommandService;
    private final CategoryQueryService categoryQueryService;

    public PublicationCategoryController(CategoryCommandService categoryCommandService,
                                        CategoryQueryService categoryQueryService) {
        this.categoryCommandService = categoryCommandService;
        this.categoryQueryService = categoryQueryService;
    }

    /**
     * Get categories for a publication
     * @param publicationId the publication ID
     * @return list of category resources
     */
    @GetMapping("/publication/{publicationId}/categories")
    public ResponseEntity<List<CategoryResource>> getCategoriesByPublicationId(@PathVariable Long publicationId) {
        var query = new GetCategoriesByPublicationIdQuery(publicationId);
        List<Category> categories = categoryQueryService.handle(query);

        List<CategoryResource> resources = categories.stream()
                .map(CategoryResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resources);
    }

    /**
     * Get publication IDs for a category
     * @param categoryId the category ID
     * @return list of publication IDs
     */
    @GetMapping("/category/{categoryId}/publications")
    public ResponseEntity<List<Long>> getPublicationsByCategoryId(@PathVariable Long categoryId) {
        var query = new GetPublicationsByCategoryIdQuery(categoryId);
        List<PublicationId> publicationIds = categoryQueryService.handle(query);

        List<Long> ids = publicationIds.stream()
                .map(PublicationId::getPublicationId)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ids);
    }

    /**
     * Assign a category to a publication
     * @param publicationId the publication ID
     * @param request the request with category ID
     * @return created response
     */
    @PostMapping("/publication/{publicationId}/assign")
    public ResponseEntity<Void> assignCategoryToPublication(
            @PathVariable Long publicationId,
            @RequestBody AssignCategoryRequest request) {
        var command = new AssignCategoryToPublicationCommand(publicationId, request.getCategoryId());
        categoryCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Remove a category from a publication
     * @param publicationId the publication ID
     * @param categoryId the category ID
     * @return no content response
     */
    @DeleteMapping("/publication/{publicationId}/category/{categoryId}")
    public ResponseEntity<Void> removeCategoryFromPublication(
            @PathVariable Long publicationId,
            @PathVariable Long categoryId) {
        var command = new RemoveCategoryFromPublicationCommand(publicationId, categoryId);
        categoryCommandService.handle(command);
        return ResponseEntity.noContent().build();
    }
} 