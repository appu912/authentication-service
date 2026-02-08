package com.progmatic.auth.wrapper;

import com.progmatic.auth.utils.RegisterUserRequest;
import com.progmatic.auth.utils.ValidationErrorResponse;

public class ValidationErrorTestCase {
  private String name;
  private RegisterUserRequest input;
  private ValidationErrorResponse expected;

  public String getName() {
    return name;
  }

  public ValidationErrorResponse getExpected() {
    return expected;
  }

  public RegisterUserRequest getInput() {
    return input;
  }
}
