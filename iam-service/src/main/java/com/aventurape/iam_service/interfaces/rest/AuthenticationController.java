package com.aventurape.iam_service.interfaces.rest;

import com.aventurape.iam_service.domain.services.UserCommandService;
import com.aventurape.iam_service.interfaces.rest.resources.AuthenticatedUserResource;
import com.aventurape.iam_service.interfaces.rest.resources.SignInResource;
import com.aventurape.iam_service.interfaces.rest.resources.SignUpResource;
import com.aventurape.iam_service.interfaces.rest.resources.UserResource;
import com.aventurape.iam_service.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.aventurape.iam_service.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import com.aventurape.iam_service.interfaces.rest.transform.SignUpCommandFromResourceAssembler;
import com.aventurape.iam_service.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/v1/authentication")
public class AuthenticationController {

    private final UserCommandService userCommandService;

    public AuthenticationController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    /**
     * Handles the sign-in request.
     * @param signInResource the sign-in request body.
     * @return the authenticated user resource.
     */
    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticatedUserResource> signIn(
            @RequestBody SignInResource signInResource) {

        var signInCommand = SignInCommandFromResourceAssembler
                .toCommandFromResource(signInResource);
        var authenticatedUser = userCommandService.handle(signInCommand);
        if (authenticatedUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler
                .toResourceFromEntity(
                        authenticatedUser.get().getLeft(), authenticatedUser.get().getRight());
        return ResponseEntity.ok(authenticatedUserResource);
    }


    /**
     * Handles the sign-up request.
     * @param signUpResource the sign-up request body.
     * @return the created user resource.
     */
    @PostMapping("/sign-up")
    public ResponseEntity<UserResource> signUp(@RequestBody SignUpResource signUpResource) {
        var signUpCommand = SignUpCommandFromResourceAssembler
                .toCommandFromResource(signUpResource);
        var user = userCommandService.handle(signUpCommand);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return new ResponseEntity<>(userResource, HttpStatus.CREATED);
    }
}