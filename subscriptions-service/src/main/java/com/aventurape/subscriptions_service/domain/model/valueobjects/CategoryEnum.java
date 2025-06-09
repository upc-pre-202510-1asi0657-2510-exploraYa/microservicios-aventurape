package com.aventurape.subscriptions_service.domain.model.valueobjects;

/**
 * Enum que representa las categorías predefinidas en el sistema
 * Facilita el trabajo con categorías sin necesidad de recordar IDs numéricos
 */
public enum CategoryEnum {
    HIKING_AND_TREKKING(1L, "Senderismo y Trekking"),
    MOUNTAINEERING_AND_CLIMBING(2L, "Montañismo y Escalada"),
    WATER_ADVENTURES(3L, "Aventuras Acuáticas"),
    CYCLING_AND_MOUNTAIN_BIKE(4L, "Cicloturismo y Mountain Bike"),
    LAND_TRAVEL_AND_OVERLAND(5L, "Viajes por Tierra y Overland"),
    AIR_SPORTS(6L, "Deportes Aéreos"),
    WINTER_ADVENTURES(7L, "Aventuras de Invierno"),
    EXPLORATION_AND_DISCOVERY(8L, "Exploración y Descubrimiento"),
    ECOTOURISM_AND_WILDLIFE(9L, "Ecoturismo y Observación de Fauna"),
    CULTURAL_AND_LOCAL_EXPERIENCES(10L, "Cultural y Experiencias Locales");

    private final Long id;
    private final String name;

    CategoryEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static CategoryEnum getByName(String name) {
        for (CategoryEnum category : values()) {
            if (category.name.equals(name)) {
                return category;
            }
        }
        return null;
    }

    public static CategoryEnum getById(Long id) {
        for (CategoryEnum category : values()) {
            if (category.id.equals(id)) {
                return category;
            }
        }
        return null;
    }
} 