package com.aventurape.subscriptions_service.interfaces.acl;

import com.aventurape.subscriptions_service.domain.model.commands.AssignCategoryToPublicationCommand;
import com.aventurape.subscriptions_service.domain.model.commands.RemoveCategoryFromPublicationCommand;
import com.aventurape.subscriptions_service.domain.model.commands.SubscribeToCategoryCommand;
import com.aventurape.subscriptions_service.domain.model.commands.UnsubscribeFromCategoryCommand;
import com.aventurape.subscriptions_service.domain.model.queries.*;
import com.aventurape.subscriptions_service.domain.services.CategoryCommandService;
import com.aventurape.subscriptions_service.domain.services.CategoryQueryService;
import com.aventurape.subscriptions_service.interfaces.acl.transform.CategoryDto;
import com.aventurape.subscriptions_service.interfaces.acl.transform.CategoryDtoAssembler;
import com.aventurape.subscriptions_service.interfaces.acl.transform.PublicationIdDto;
import com.aventurape.subscriptions_service.interfaces.acl.transform.PublicationIdDtoAssembler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CategoryContextFacade
 * <p>
 *     Esta clase es una fachada para el contexto de Category. Proporciona una interfaz
 *     simple para que otros bounded contexts interactúen con el contexto de Category.
 *     Esta clase es parte de la capa ACL (Anti-Corruption Layer).
 * </p>
 */
@Service
public class CategoryContextFacade {
    private final CategoryCommandService categoryCommandService;
    private final CategoryQueryService categoryQueryService;

    public CategoryContextFacade(CategoryCommandService categoryCommandService,
                               CategoryQueryService categoryQueryService) {
        this.categoryCommandService = categoryCommandService;
        this.categoryQueryService = categoryQueryService;
    }

    /**
     * Obtiene todas las categorías
     * @return lista de DTOs de categorías
     */
    public List<CategoryDto> getAllCategories() {
        var query = new GetAllCategoriesQuery();
        return categoryQueryService.handle(query)
                .stream()
                .map(CategoryDtoAssembler::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una categoría por su ID
     * @param categoryId ID de la categoría
     * @return DTO de la categoría si existe
     */
    public Optional<CategoryDto> getCategoryById(Long categoryId) {
        var query = new GetCategoryByIdQuery(categoryId);
        return categoryQueryService.handle(query)
                .map(CategoryDtoAssembler::toDto);
    }

    /**
     * Obtiene las categorías a las que está suscrito un usuario
     * @param userId ID del usuario
     * @return lista de DTOs de categorías
     */
    public List<CategoryDto> getSubscribedCategoriesByUserId(Long userId) {
        var query = new GetSubscribedCategoriesByUserIdQuery(userId);
        return categoryQueryService.handle(query)
                .stream()
                .map(CategoryDtoAssembler::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene las categorías asignadas a una publicación
     * @param publicationId ID de la publicación
     * @return lista de DTOs de categorías
     */
    public List<CategoryDto> getCategoriesByPublicationId(Long publicationId) {
        var query = new GetCategoriesByPublicationIdQuery(publicationId);
        return categoryQueryService.handle(query)
                .stream()
                .map(CategoryDtoAssembler::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los IDs de publicaciones para una categoría
     * @param categoryId ID de la categoría
     * @return lista de DTOs con IDs de publicaciones
     */
    public List<PublicationIdDto> getPublicationIdsByCategoryId(Long categoryId) {
        var query = new GetPublicationsByCategoryIdQuery(categoryId);
        return categoryQueryService.handle(query)
                .stream()
                .map(PublicationIdDtoAssembler::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene las categorías más populares
     * @param limit número máximo de categorías a retornar
     * @return lista de DTOs de categorías ordenadas por popularidad
     */
    public List<CategoryDto> getPopularCategories(int limit) {
        var query = new GetPopularCategoriesQuery(limit);
        return categoryQueryService.handle(query)
                .stream()
                .map(CategoryDtoAssembler::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Suscribe a un usuario a una categoría
     * @param userId ID del usuario
     * @param categoryId ID de la categoría
     */
    public void subscribeUserToCategory(Long userId, Long categoryId) {
        var command = new SubscribeToCategoryCommand(userId, categoryId);
        categoryCommandService.handle(command);
    }

    /**
     * Cancela la suscripción de un usuario a una categoría
     * @param userId ID del usuario
     * @param categoryId ID de la categoría
     */
    public void unsubscribeUserFromCategory(Long userId, Long categoryId) {
        var command = new UnsubscribeFromCategoryCommand(userId, categoryId);
        categoryCommandService.handle(command);
    }

    /**
     * Asigna una categoría a una publicación
     * @param publicationId ID de la publicación
     * @param categoryId ID de la categoría
     */
    public void assignCategoryToPublication(Long publicationId, Long categoryId) {
        var command = new AssignCategoryToPublicationCommand(publicationId, categoryId);
        categoryCommandService.handle(command);
    }

    /**
     * Elimina una categoría de una publicación
     * @param publicationId ID de la publicación
     * @param categoryId ID de la categoría
     */
    public void removeCategoryFromPublication(Long publicationId, Long categoryId) {
        var command = new RemoveCategoryFromPublicationCommand(publicationId, categoryId);
        categoryCommandService.handle(command);
    }
} 