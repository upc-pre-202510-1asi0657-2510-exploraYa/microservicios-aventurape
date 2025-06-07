package com.aventurape.iam_service.domain.services;

import com.aventurape.iam_service.domain.model.aggregates.User;
import com.aventurape.iam_service.domain.model.commands.SignInCommand;
import com.aventurape.iam_service.domain.model.commands.SignUpCommand;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Optional;

public interface UserCommandService {
    Optional<ImmutablePair<User, String>> handle(SignInCommand command);
    Optional<User> handle(SignUpCommand command);
}