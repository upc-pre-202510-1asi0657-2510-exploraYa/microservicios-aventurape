package com.aventurape.iam_service.domain.services;

import com.aventurape.iam_service.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {
    void handle(SeedRolesCommand command);
}

