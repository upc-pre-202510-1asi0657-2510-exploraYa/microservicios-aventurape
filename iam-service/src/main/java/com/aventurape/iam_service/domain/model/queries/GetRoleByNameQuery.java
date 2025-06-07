package com.aventurape.iam_service.domain.model.queries;

import com.aventurape.iam_service.domain.model.valueobjects.Roles;

public record GetRoleByNameQuery(Roles name) {
}
