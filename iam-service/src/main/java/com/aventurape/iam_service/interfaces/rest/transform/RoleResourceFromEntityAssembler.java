package com.aventurape.iam_service.interfaces.rest.transform;


import com.aventurape.iam_service.domain.model.entities.Role;
import com.aventurape.iam_service.interfaces.rest.resources.RoleResource;

public class RoleResourceFromEntityAssembler {

  public static RoleResource toResourceFromEntity(Role role) {
    return new RoleResource(role.getId(), role.getStringName());
  }
}
