package com.progmatic.auth.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ConfirmPassword(message = "Passwords do not match.")
public class RegisterUserRequest {
  @NotBlank(message = "Email is required.") private String email;

  @NotBlank(message = "Password is required.") private String password;

  @NotBlank(message = "Confirm password is required.") @JsonProperty(value = "confirm_password")
  private String confirmPassword;

  private RegisterUserRequest(Builder b) {
    this.email = b.email;
    this.password = b.password;
    this.confirmPassword = b.confirmPassword;
  }

  public static class Builder {
    private String email;
    private String password;
    private String confirmPassword;

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder confirmPassword(String confirmPassword) {
      this.confirmPassword = confirmPassword;
      return this;
    }

    public RegisterUserRequest build() {
      return new RegisterUserRequest(this);
    }
  }
}
