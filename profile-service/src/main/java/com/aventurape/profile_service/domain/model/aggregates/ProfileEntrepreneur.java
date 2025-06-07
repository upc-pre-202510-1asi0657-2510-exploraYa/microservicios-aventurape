package com.aventurape.profile_service.domain.model.aggregates;

import com.aventurape.profile_service.domain.model.commands.CreateProfileEntrepreneurCommand;
import com.aventurape.profile_service.domain.model.valueobjects.NameEntrepreneurship;
import com.aventurape.profile_service.domain.model.valueobjects.UserId;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity
public class ProfileEntrepreneur extends Profile {
    @Embedded
    private NameEntrepreneurship name;

    @Embedded
    private UserId userId;
    private String email;
    private String street;
    private String number;
    private String city;
    private String postalCode;
    private String country;


    public ProfileEntrepreneur() {
        super();
    }

    public ProfileEntrepreneur(UserId userId, String email, String street, String number, String city, String postalCode, String country, String name) {
        this.userId = userId;
        this.email = email;
        this.street = street;
        this.number = number;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.name = new NameEntrepreneurship(name);
    }

    public ProfileEntrepreneur(CreateProfileEntrepreneurCommand command) {
        super(command.email(), command.street(), command.number(), command.city(), command.postalCode(), command.country());
        this.userId = new UserId(command.userId());
        this.city = command.city();
        this.country = command.country();
        this.email = command.email();
        this.street = command.street();
        this.number = command.number();
        this.postalCode = command.postalCode();
        this.name = new NameEntrepreneurship(command.name());
    }

    public UserId getUserId() {
        return userId;
    }

    public String getName() {
        return name.nameEntrepreneurship();
    }
}