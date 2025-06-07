package com.aventurape.profile_service.infrastructure.persistence.jpa.repositories;

import com.aventurape.profile_service.domain.model.aggregates.Profile;
import com.aventurape.profile_service.domain.model.aggregates.ProfileAdventurer;
import com.aventurape.profile_service.domain.model.aggregates.ProfileEntrepreneur;
import com.aventurape.profile_service.domain.model.valueobjects.EmailAddress;
import com.aventurape.profile_service.domain.model.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query("SELECT p FROM ProfileAdventurer p")
    List<ProfileAdventurer> findAllAdventurers();

    @Query("SELECT p FROM ProfileEntrepreneur p")
    List<ProfileEntrepreneur> findAllEntrepreneurs();

    @Query("SELECT p FROM ProfileEntrepreneur p WHERE p.email.address = :email")
    Optional<ProfileEntrepreneur> findEntrepreneurByEmail(@Param("email") String email);

    @Query("SELECT p FROM ProfileAdventurer p WHERE p.email.address = :email")
    Optional<ProfileAdventurer> findAdventurerByEmail(@Param("email") String email);

    @Query("SELECT p FROM ProfileAdventurer p WHERE p.id = :id")
    Optional<ProfileAdventurer> findAdventurerById(@Param("id") Long id);

    @Query("SELECT p FROM ProfileEntrepreneur p WHERE p.id = :id")
    Optional<ProfileEntrepreneur> findEntrepreneurById(@Param("id") Long id);

    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM ProfileAdventurer p WHERE p.email.address = :email) THEN TRUE ELSE FALSE END FROM ProfileAdventurer p")
    Boolean existsAdventurerByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM ProfileEntrepreneur p WHERE p.email.address = :email) THEN TRUE ELSE FALSE END FROM ProfileEntrepreneur p")
    Boolean existsEntrepreneurByEmail(@Param("email") String email);

    @Query("SELECT p FROM ProfileAdventurer p WHERE p.userId = :userId")
    Optional<ProfileAdventurer> findAdventurerByUserId(@Param("userId") UserId userId);

    @Query("SELECT p FROM ProfileEntrepreneur p WHERE p.userId = :userId")
    Optional<ProfileEntrepreneur> findEntrepreneurByUserId(@Param("userId") UserId userId);

    List<ProfileAdventurer> findByEmail(String email);
    List<ProfileAdventurer> findByUserId(long userId);
}
