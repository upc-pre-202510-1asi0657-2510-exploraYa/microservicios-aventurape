package com.aventurape.iam_service.domain.model.commands;

import com.aventurape.iam_service.domain.model.entities.Role;

import java.util.List;

public record SignUpCommand(String username, String password, List<Role> roles) {
}
