package com.aventurape.iam_service.infraestructure.persistence.jpa.repositories;

import com.aventurape.iam_service.domain.model.entities.Role;
import com.aventurape.iam_service.domain.model.valueobjects.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(Roles name);
    boolean existsByName(Roles name);
}
