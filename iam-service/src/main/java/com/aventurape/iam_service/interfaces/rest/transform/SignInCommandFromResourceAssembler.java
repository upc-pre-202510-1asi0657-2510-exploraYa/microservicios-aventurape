package com.aventurape.iam_service.interfaces.rest.transform;


import com.aventurape.iam_service.domain.model.commands.SignInCommand;
import com.aventurape.iam_service.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {

  public static SignInCommand toCommandFromResource(SignInResource signInResource) {
    return new SignInCommand(signInResource.username(), signInResource.password());
  }
}
