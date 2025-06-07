package com.aventurape.iam_service.domain.services;

import com.aventurape.iam_service.domain.model.entities.Role;
import com.aventurape.iam_service.domain.model.queries.GetAllRolesQuery;
import com.aventurape.iam_service.domain.model.queries.GetRoleByNameQuery;

import java.util.List;
import java.util.Optional;

public interface RoleQueryService {
    List<Role> handle(GetAllRolesQuery query);
    Optional<Role> handle(GetRoleByNameQuery query);
}

