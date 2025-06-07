package com.aventurape.iam_service.interfaces.rest.transform;


import com.aventurape.iam_service.domain.model.aggregates.User;
import com.aventurape.iam_service.interfaces.rest.resources.AuthenticatedUserResource;

public class AuthenticatedUserResourceFromEntityAssembler {

  public static AuthenticatedUserResource toResourceFromEntity(User user, String token) {
    return new AuthenticatedUserResource(user.getId(), user.getUsername(), token);
  }
}
