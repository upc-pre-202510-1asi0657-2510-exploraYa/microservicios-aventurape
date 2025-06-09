package com.aventurape.subscriptions_service.infrastructure.data;

import com.aventurape.subscriptions_service.domain.model.aggregates.Category;
import com.aventurape.subscriptions_service.domain.model.valueobjects.CategoryEnum;
import com.aventurape.subscriptions_service.infrastructure.persistence.jpa.repositories.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Inicializador para cargar las categorías predefinidas en la base de datos
 */
@Configuration
public class CategoryDataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(CategoryDataInitializer.class);

    /**
     * Inicializa las categorías predefinidas en la base de datos
     * @param categoryRepository repositorio de categorías
     * @return un CommandLineRunner que inicializa las categorías
     */
    @Bean
    public CommandLineRunner initCategoryData(CategoryRepository categoryRepository) {
        return args -> {
            logger.info("Inicializando categorías predefinidas...");
            
            // Verificar si ya existen categorías
            if (categoryRepository.count() > 0) {
                logger.info("Las categorías ya están inicializadas. Total: {}", categoryRepository.count());
                return;
            }
            
            // Inicializar categorías desde el enum
            Arrays.stream(CategoryEnum.values()).forEach(categoryEnum -> {
                // Verificar si la categoría ya existe
                if (categoryRepository.findByName(categoryEnum.getName()).isEmpty()) {
                    Category category = new Category(categoryEnum.getName());
                    categoryRepository.save(category);
                    logger.info("Categoría creada: {}", category.getName());
                }
            });
            
            logger.info("Inicialización de categorías completada. Total: {}", categoryRepository.count());
        };
    }
} 