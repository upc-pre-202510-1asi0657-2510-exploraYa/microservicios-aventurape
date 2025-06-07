package com.aventurape.iam_service.interfaces.rest.transform;


import com.aventurape.iam_service.domain.model.commands.SignUpCommand;
import com.aventurape.iam_service.domain.model.entities.Role;
import com.aventurape.iam_service.interfaces.rest.resources.SignUpResource;

import java.util.ArrayList;

public class SignUpCommandFromResourceAssembler {

  public static SignUpCommand toCommandFromResource(SignUpResource resource) {
    var roles = resource.roles() != null
        ? resource.roles().stream().map(name -> Role.toRoleFromName(name)).toList()
        : new ArrayList<Role>();
    return new SignUpCommand(resource.username(), resource.password(), roles);
  }
}
