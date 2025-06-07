package com.aventurape.iam_service.interfaces.rest.transform;


import com.aventurape.iam_service.domain.model.aggregates.User;
import com.aventurape.iam_service.domain.model.entities.Role;
import com.aventurape.iam_service.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {

  public static UserResource toResourceFromEntity(User user) {
    var roles = user.getRoles().stream()
        .map(Role::getStringName)
        .toList();
    return new UserResource(user.getId(), user.getUsername(), roles);
  }
}
