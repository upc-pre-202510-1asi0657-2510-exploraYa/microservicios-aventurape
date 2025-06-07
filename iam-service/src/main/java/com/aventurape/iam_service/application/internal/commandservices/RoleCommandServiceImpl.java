package com.aventurape.iam_service.application.internal.commandservices;

import com.aventurape.iam_service.domain.model.commands.SeedRolesCommand;
import com.aventurape.iam_service.domain.model.entities.Role;
import com.aventurape.iam_service.domain.model.valueobjects.Roles;
import com.aventurape.iam_service.domain.services.RoleCommandService;
import com.aventurape.iam_service.infraestructure.persistence.jpa.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class RoleCommandServiceImpl implements RoleCommandService {

    private final RoleRepository roleRepository;

    public RoleCommandServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Override
    public void handle(SeedRolesCommand command) {
        Arrays.stream(Roles.values())
                .forEach(role -> {
                    if(!roleRepository.existsByName(role)) {
                        roleRepository.save(new Role(Roles.valueOf(role.name())));
                    }
                } );
    }
}