package com.aventurape.subscriptions_service.application.internal.commandservices;

import com.aventurape.subscriptions_service.domain.model.aggregates.Category;
import com.aventurape.subscriptions_service.domain.model.commands.AssignCategoryToPublicationCommand;
import com.aventurape.subscriptions_service.domain.model.commands.RemoveCategoryFromPublicationCommand;
import com.aventurape.subscriptions_service.domain.model.commands.SubscribeToCategoryCommand;
import com.aventurape.subscriptions_service.domain.model.commands.UnsubscribeFromCategoryCommand;
import com.aventurape.subscriptions_service.domain.model.entities.CategorySubscription;
import com.aventurape.subscriptions_service.domain.model.entities.PublicationCategory;
import com.aventurape.subscriptions_service.domain.model.valueobjects.PublicationId;
import com.aventurape.subscriptions_service.domain.model.valueobjects.UserId;
import com.aventurape.subscriptions_service.domain.services.CategoryCommandService;
import com.aventurape.subscriptions_service.infrastructure.persistence.jpa.repositories.CategoryRepository;
import com.aventurape.subscriptions_service.infrastructure.persistence.jpa.repositories.CategorySubscriptionRepository;
import com.aventurape.subscriptions_service.infrastructure.persistence.jpa.repositories.PublicationCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CategoryCommandServiceImpl implements CategoryCommandService {

    private final Logger logger = LoggerFactory.getLogger(CategoryCommandServiceImpl.class);
    private final CategoryRepository categoryRepository;
    private final CategorySubscriptionRepository categorySubscriptionRepository;
    private final PublicationCategoryRepository publicationCategoryRepository;

    public CategoryCommandServiceImpl(CategoryRepository categoryRepository,
                                      CategorySubscriptionRepository categorySubscriptionRepository,
                                      PublicationCategoryRepository publicationCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.categorySubscriptionRepository = categorySubscriptionRepository;
        this.publicationCategoryRepository = publicationCategoryRepository;
    }

    @Override
    @Transactional
    public CategorySubscription handle(SubscribeToCategoryCommand command) {
        logger.info("Manejando comando de suscripción: userId={}, categoryId={}",
                command.userId(), command.categoryId());

        // Find the category
        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> {
                    logger.error("La categoría con ID {} no fue encontrada", command.categoryId());
                    return new IllegalArgumentException("Category not found");
                });

        logger.debug("Categoría encontrada: {}", category.getName());

        // Crear value object UserId
        UserId userId = new UserId(command.userId());
        logger.debug("UserId creado: {}", userId.getUserId());

        // Check if subscription already exists
        Optional<CategorySubscription> existingSubscription =
                categorySubscriptionRepository.findByUserIdAndCategoryId(userId, command.categoryId());

        if (existingSubscription.isPresent()) {
            logger.info("La suscripción ya existe para userId={}, categoryId={}",
                    command.userId(), command.categoryId());
            return existingSubscription.get();
        }

        // Create new subscription
        CategorySubscription subscription = new CategorySubscription(userId, category);
        logger.debug("Nueva suscripción creada");

        // Increment subscriber count
        category.incrementSubscribersCount();
        categoryRepository.save(category);
        logger.debug("Contador de suscriptores incrementado: {}", category.getSubscribersCount());

        // Save and return subscription
        CategorySubscription savedSubscription = categorySubscriptionRepository.save(subscription);
        logger.info("Suscripción guardada correctamente con ID: {}", savedSubscription.getId());

        return savedSubscription;
    }

    @Override
    @Transactional
    public void handle(UnsubscribeFromCategoryCommand command) {
        logger.info("Manejando comando de cancelación de suscripción: userId={}, categoryId={}",
                command.userId(), command.categoryId());

        UserId userId = new UserId(command.userId());

        // Find the subscription
        Optional<CategorySubscription> subscription =
                categorySubscriptionRepository.findByUserIdAndCategoryId(userId, command.categoryId());

        if (subscription.isPresent()) {
            CategorySubscription sub = subscription.get();

            // Decrement subscriber count
            Category category = sub.getCategory();
            category.decrementSubscribersCount();
            categoryRepository.save(category);
            logger.debug("Contador de suscriptores decrementado: {}", category.getSubscribersCount());

            // Delete subscription
            categorySubscriptionRepository.delete(sub);
            logger.info("Suscripción eliminada correctamente para userId={}, categoryId={}",
                    command.userId(), command.categoryId());
        } else {
            logger.warn("No se encontró suscripción para cancelar: userId={}, categoryId={}",
                    command.userId(), command.categoryId());
        }
    }

    @Override
    @Transactional
    public PublicationCategory handle(AssignCategoryToPublicationCommand command) {
        logger.info("Asignando categoría a publicación: publicationId={}, categoryId={}",
                command.publicationId(), command.categoryId());

        // Find the category
        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> {
                    logger.error("La categoría con ID {} no fue encontrada", command.categoryId());
                    return new IllegalArgumentException("Category not found");
                });

        PublicationId publicationId = new PublicationId(command.publicationId());

        // Create publication category relationship
        PublicationCategory publicationCategory = new PublicationCategory(publicationId, category);

        // Save and return
        PublicationCategory saved = publicationCategoryRepository.save(publicationCategory);
        logger.info("Categoría asignada correctamente a publicación");

        return saved;
    }

    @Override
    @Transactional
    public void handle(RemoveCategoryFromPublicationCommand command) {
        logger.info("Removiendo categoría de publicación: publicationId={}, categoryId={}",
                command.publicationId(), command.categoryId());

        PublicationId publicationId = new PublicationId(command.publicationId());

        // Find matching relationships
        publicationCategoryRepository.findByPublicationId(publicationId)
                .stream()
                .filter(pc -> pc.getCategory().getId().equals(command.categoryId()))
                .forEach(pc -> {
                    publicationCategoryRepository.delete(pc);
                    logger.debug("Relación publicación-categoría eliminada: publicationCategoryId={}", pc.getId());
                });

        logger.info("Proceso de eliminación completado");
    }
} 