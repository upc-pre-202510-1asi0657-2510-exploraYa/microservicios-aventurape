package com.aventurape.subscriptions_service.application.internal.queryservices;

import com.aventurape.subscriptions_service.domain.model.aggregates.Category;
import com.aventurape.subscriptions_service.domain.model.entities.CategorySubscription;
import com.aventurape.subscriptions_service.domain.model.entities.PublicationCategory;
import com.aventurape.subscriptions_service.domain.model.queries.*;
import com.aventurape.subscriptions_service.domain.model.valueobjects.PublicationId;
import com.aventurape.subscriptions_service.domain.model.valueobjects.UserId;
import com.aventurape.subscriptions_service.domain.services.CategoryQueryService;
import com.aventurape.subscriptions_service.infrastructure.persistence.jpa.repositories.CategoryRepository;
import com.aventurape.subscriptions_service.infrastructure.persistence.jpa.repositories.CategorySubscriptionRepository;
import com.aventurape.subscriptions_service.infrastructure.persistence.jpa.repositories.PublicationCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of CategoryQueryService
 */
@Service
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final Logger logger = LoggerFactory.getLogger(CategoryQueryServiceImpl.class);
    private final CategoryRepository categoryRepository;
    private final CategorySubscriptionRepository categorySubscriptionRepository;
    private final PublicationCategoryRepository publicationCategoryRepository;

    public CategoryQueryServiceImpl(CategoryRepository categoryRepository,
                                   CategorySubscriptionRepository categorySubscriptionRepository,
                                   PublicationCategoryRepository publicationCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.categorySubscriptionRepository = categorySubscriptionRepository;
        this.publicationCategoryRepository = publicationCategoryRepository;
    }

    @Override
    public List<Category> handle(GetAllCategoriesQuery query) {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> handle(GetCategoryByIdQuery query) {
        return categoryRepository.findById(query.categoryId());
    }

    @Override
    public List<Category> handle(GetSubscribedCategoriesByUserIdQuery query) {
        Long userId = query.userId();
        logger.info("Buscando suscripciones para el userId: {}", userId);
        
        // Creamos el value object UserId
        UserId userIdVO = new UserId(userId);
        logger.debug("UserId value object creado con valor: {}", userIdVO.getUserId());
        
        // Obtenemos las suscripciones para este usuario
        List<CategorySubscription> subscriptions = categorySubscriptionRepository.findByUserId(userIdVO);
        logger.info("Encontradas {} suscripciones para el userId: {}", subscriptions.size(), userId);
        
        if (subscriptions.isEmpty()) {
            logger.warn("No se encontraron suscripciones para el userId: {}", userId);
        }
        
        // Extraemos las categorías de las suscripciones
        List<Category> categories = subscriptions.stream()
                .map(subscription -> {
                    Category category = subscription.getCategory();
                    logger.debug("Categoría encontrada en suscripción: ID={}, Nombre={}", 
                            category.getId(), category.getName());
                    return category;
                })
                .collect(Collectors.toList());
        
        return categories;
    }

    @Override
    public List<Category> handle(GetCategoriesByPublicationIdQuery query) {
        PublicationId publicationId = new PublicationId(query.publicationId());
        return publicationCategoryRepository.findByPublicationId(publicationId)
                .stream()
                .map(PublicationCategory::getCategory)
                .collect(Collectors.toList());
    }

    @Override
    public List<PublicationId> handle(GetPublicationsByCategoryIdQuery query) {
        return publicationCategoryRepository.findByCategoryId(query.categoryId())
                .stream()
                .map(PublicationCategory::getPublicationId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> handle(GetPopularCategoriesQuery query) {
        return categoryRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Category::getSubscribersCount).reversed())
                .limit(query.limit())
                .collect(Collectors.toList());
    }
} 