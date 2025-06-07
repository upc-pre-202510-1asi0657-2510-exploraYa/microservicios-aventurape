package com.aventurape.iam_service.interfaces.rest;

import com.aventurape.iam_service.domain.model.queries.GetAllUsersQuery;
import com.aventurape.iam_service.domain.model.queries.GetUserByIdQuery;
import com.aventurape.iam_service.domain.services.UserCommandService;
import com.aventurape.iam_service.domain.services.UserQueryService;
import com.aventurape.iam_service.interfaces.rest.resources.UserResource;
import com.aventurape.iam_service.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UsersController {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    public UsersController(UserQueryService userQueryService, UserCommandService userCommandService)
    {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    @GetMapping
    public ResponseEntity<List<UserResource>> getAllUsers() {
        var getAllUsersQuery = new GetAllUsersQuery();
        var users = userQueryService.handle(getAllUsersQuery);
        var userResources = users.stream()
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(userResources);
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserResource> getUserById(@PathVariable Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }


}
