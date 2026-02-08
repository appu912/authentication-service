package com.progmatic.auth.utils;

import java.util.UUID;
import lombok.Data;

@Data
public class RegisterUserResponse {
  private UUID userId;
  private String email;
  private String message;

  private RegisterUserResponse(Builder b) {
    this.userId = b.userId;
    this.email = b.email;
    this.message = b.message;
  }

  public static class Builder {
    private UUID userId;
    private String email;
    private String message;

    public Builder userId(UUID userId) {
      this.userId = userId;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder message(String message) {
      this.message = message;
      return this;
    }

    public RegisterUserResponse build() {
      return new RegisterUserResponse(this);
    }
  }
}
