package com.progmatic.auth.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConfirmPasswordValidator
    implements ConstraintValidator<ConfirmPassword, RegisterUserRequest> {

  private String message;

  @Override
  public void initialize(ConfirmPassword constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    this.message = constraintAnnotation.message();
  }

  @Override
  public boolean isValid(
      RegisterUserRequest request, ConstraintValidatorContext constraintValidatorContext) {
    if (request.getPassword() != null
        && request.getConfirmPassword() != null
        && !request.getPassword().isEmpty()
        && !request.getConfirmPassword().isEmpty()
        && request.getPassword().equals(request.getConfirmPassword())) {
      return true;
    }
    constraintValidatorContext.disableDefaultConstraintViolation();
    constraintValidatorContext
        .buildConstraintViolationWithTemplate(this.message)
        .addPropertyNode("confirmPassword")
        .addConstraintViolation();
    return false;
  }
}
