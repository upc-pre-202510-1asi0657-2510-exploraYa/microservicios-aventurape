package com.aventurape.subscriptions_service.interfaces.rest;

import com.aventurape.subscriptions_service.domain.model.aggregates.Category;
import com.aventurape.subscriptions_service.domain.model.queries.GetAllCategoriesQuery;
import com.aventurape.subscriptions_service.domain.model.queries.GetCategoryByIdQuery;
import com.aventurape.subscriptions_service.domain.model.queries.GetPopularCategoriesQuery;
import com.aventurape.subscriptions_service.domain.services.CategoryCommandService;
import com.aventurape.subscriptions_service.domain.services.CategoryQueryService;
import com.aventurape.subscriptions_service.interfaces.rest.resources.CategoryResource;
import com.aventurape.subscriptions_service.interfaces.rest.transform.CategoryResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for categories
 */
@RestController
@RequestMapping(value = "/api/v1/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Categories")
public class CategoryController {

    private final CategoryCommandService categoryCommandService;
    private final CategoryQueryService categoryQueryService;

    public CategoryController(CategoryCommandService categoryCommandService,
                             CategoryQueryService categoryQueryService) {
        this.categoryCommandService = categoryCommandService;
        this.categoryQueryService = categoryQueryService;
    }

    /**
     * Get all categories
     * @return list of category resources
     */
    @GetMapping
    public ResponseEntity<List<CategoryResource>> getAllCategories() {
        var query = new GetAllCategoriesQuery();
        List<Category> categories = categoryQueryService.handle(query);
        
        List<CategoryResource> resources = categories.stream()
                .map(CategoryResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(resources);
    }

    /**
     * Get a category by ID
     * @param categoryId the category ID
     * @return the category resource if found
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResource> getCategoryById(@PathVariable Long categoryId) {
        var query = new GetCategoryByIdQuery(categoryId);
        Optional<Category> category = categoryQueryService.handle(query);
        
        return category.map(c -> ResponseEntity.ok(
                CategoryResourceFromEntityAssembler.toResourceFromEntity(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get popular categories
     * @param limit maximum number of categories to return
     * @return list of category resources ordered by popularity
     */
    @GetMapping("/popular")
    public ResponseEntity<List<CategoryResource>> getPopularCategories(
            @RequestParam(defaultValue = "10") Integer limit) {
        var query = new GetPopularCategoriesQuery(limit);
        List<Category> categories = categoryQueryService.handle(query);
        
        List<CategoryResource> resources = categories.stream()
                .map(CategoryResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(resources);
    }
} 