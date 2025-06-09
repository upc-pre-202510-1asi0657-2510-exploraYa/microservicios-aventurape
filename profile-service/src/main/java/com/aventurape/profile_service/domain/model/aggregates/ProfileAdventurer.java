package com.aventurape.profile_service.domain.model.aggregates;

import com.aventurape.profile_service.domain.model.commands.CreateProfileAdventurerCommand;
import com.aventurape.profile_service.domain.model.valueobjects.Gender;
import com.aventurape.profile_service.domain.model.valueobjects.Genders;
import com.aventurape.profile_service.domain.model.valueobjects.PersonName;
import com.aventurape.profile_service.domain.model.valueobjects.UserId;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class ProfileAdventurer extends Profile {

    @Embedded
    private PersonName name;

    @Embedded
    private UserId userId;

    @Enumerated(EnumType.STRING)
    private Genders gender;

    public ProfileAdventurer() {
        super();
    }

    public ProfileAdventurer(UserId userId, String firstName, String lastName,
                             String email, String street, String number,
                             String city, String postalCode, String country,
                             String gender) {
        super(email, street, number, city, postalCode, country);
        this.userId = userId;
        this.name = new PersonName(firstName, lastName);
        this.gender = new Gender(gender).convertToEnum();
    }

    public ProfileAdventurer(CreateProfileAdventurerCommand command) {
        super(command.email(), command.street(), command.number(), command.city(), command.postalCode(), command.country());
        this.userId = new UserId(command.userId());
        this.name = new PersonName(command.firstName(), command.lastName());
        this.gender = new Gender(command.gender()).convertToEnum();
    }

    public String getGender() {
        return gender.toString();
    }

    public UserId getUserId() {
        return userId;
    }

    public String getFirstName() {
        return name.firstName();
    }

    public String getLastName() {
        return name.lastName();
    }
}