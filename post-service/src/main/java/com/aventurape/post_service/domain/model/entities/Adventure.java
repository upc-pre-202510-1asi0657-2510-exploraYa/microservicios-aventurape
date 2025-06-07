package com.aventurape.post_service.domain.model.entities;

import com.aventurape.post_service.shared.domain.model.entities.AuditableAbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "adventures")
public class Adventure extends AuditableAbstractEntity<Adventure> {

    @NotBlank
    @Column(name = "name_activity")
    private String nameActivity;

    @NotBlank
    @Column(length = 1000)
    private String description;

    @NotNull
    @Positive
    @Column(name = "time_duration")
    private Integer timeDuration;

    @NotNull
    @Positive
    @Column(name = "cant_people")
    private Integer cantPeople;

    public Adventure() {}

    public Adventure(String nameActivity, String description, Integer timeDuration, Integer cantPeople) {
        this.nameActivity = nameActivity;
        this.description = description;
        this.timeDuration = timeDuration;
        this.cantPeople = cantPeople;
    }

    public Adventure updateInformation(String nameActivity, String description, Integer timeDuration, Integer cantPeople) {
        this.nameActivity = nameActivity;
        this.description = description;
        this.timeDuration = timeDuration;
        this.cantPeople = cantPeople;
        return this;
    }
} 