package com.aventurape.iam_service.domain.services;

import com.aventurape.iam_service.domain.model.aggregates.User;
import com.aventurape.iam_service.domain.model.queries.GetAllUsersQuery;
import com.aventurape.iam_service.domain.model.queries.GetUserByIdQuery;
import com.aventurape.iam_service.domain.model.queries.GetUserByUsernameQuery;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {
    List<User> handle(GetAllUsersQuery query);
    Optional<User> handle(GetUserByIdQuery query);
    Optional<User> handle(GetUserByUsernameQuery query);
}