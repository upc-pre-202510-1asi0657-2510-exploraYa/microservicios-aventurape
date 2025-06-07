package com.aventurape.iam_service.application.internal.queryservices;

import com.aventurape.iam_service.domain.model.entities.Role;
import com.aventurape.iam_service.domain.model.queries.GetAllRolesQuery;
import com.aventurape.iam_service.domain.model.queries.GetRoleByNameQuery;
import com.aventurape.iam_service.domain.services.RoleQueryService;
import com.aventurape.iam_service.infraestructure.persistence.jpa.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleQueryServiceImpl implements RoleQueryService {
    private final RoleRepository roleRepository;
    public RoleQueryServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> handle(GetAllRolesQuery query) {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> handle(GetRoleByNameQuery query) {
        return roleRepository.findByName(query.name());
    }
}
