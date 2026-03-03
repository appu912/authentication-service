package com.progmatic.auth.service;

import com.progmatic.auth.entity.UserCredential;
import com.progmatic.auth.exception.UserAlreadyExistsException;
import com.progmatic.auth.repository.UserManagementRepository;
import com.progmatic.auth.utils.RegisterUserRequest;
import com.progmatic.auth.utils.RegisterUserResponse;
import jakarta.validation.*;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserManagementServiceTest {

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private UserManagementRepository userManagementRepository;

  private ValidatorFactory validatorFactory;

  private UserManagementService userManagementService;

  @BeforeEach
  public void setup() {
    validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();
    userManagementService =
        new UserManagementServiceImpl(userManagementRepository, passwordEncoder, validator);
  }

  @AfterEach
  public void cleanUp() {
    validatorFactory.close();
  }

  @Test
  public void testCreateAuthUser_validateEmail() {
    RegisterUserRequest emailNotPresent =
        new RegisterUserRequest.Builder()
            .password("Password@testUser1")
            .confirmPassword("Password@testUser1")
            .build();
    Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(
            () -> {
              userManagementService.createAuthUser(emailNotPresent);
            })
        .withMessage("Invalid request body. Constraints have been violated.")
        .satisfies(
            e -> {
              Assertions.assertThat(e.getConstraintViolations())
                  .extracting(ConstraintViolation::getMessage)
                  .containsExactlyInAnyOrder("Email is require.");
            });
  }

  @Test
  public void testCreateAuthUser_validatePassword() {
    RegisterUserRequest passwordNotPresent =
        new RegisterUserRequest.Builder()
            .email("test-user@gmail.com")
            .password("")
            .confirmPassword("Password@testUser1")
            .build();
    Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(
            () -> {
              userManagementService.createAuthUser(passwordNotPresent);
            })
        .withMessage("Invalid request body. Constraints have been violated.")
        .satisfies(
            e -> {
              Assertions.assertThat(e.getConstraintViolations())
                  .extracting(ConstraintViolation::getMessage)
                  .containsExactlyInAnyOrder("Password is required.", "Passwords do not match.");
            });
  }

  @Test
  public void testCreateAuthUser_validateEmptyPasswordAndConfirmPassword() {
    RegisterUserRequest passwordAndEmptyPasswordNotPresent =
        new RegisterUserRequest.Builder()
            .email("test-user@gmail.com")
            .password("")
            .confirmPassword("")
            .build();
    Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(
            () -> {
              userManagementService.createAuthUser(passwordAndEmptyPasswordNotPresent);
            })
        .withMessage("Invalid request body. Constraints have been violated.")
        .satisfies(
            e -> {
              Assertions.assertThat(e.getConstraintViolations())
                  .extracting(ConstraintViolation::getMessage)
                  .containsExactlyInAnyOrder(
                      "Password is required.",
                      "Confirm password is required.",
                      "Passwords do not match.");
            });
  }

  @Test
  public void testCreateAuthUser_validateNullPasswordAndConfirmPassword() {
    RegisterUserRequest passwordAndEmptyPasswordNull =
        new RegisterUserRequest.Builder().email("test-user@gmail.com").build();
    Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(
            () -> {
              userManagementService.createAuthUser(passwordAndEmptyPasswordNull);
            })
        .withMessage("Invalid request body. Constraints have been violated.")
        .satisfies(
            e -> {
              Assertions.assertThat(e.getConstraintViolations())
                  .extracting(ConstraintViolation::getMessage)
                  .containsExactlyInAnyOrder(
                      "Password is required.",
                      "Confirm password is required.",
                      "Passwords do not match.");
            });
  }

  @Test
  public void testCreateAuthUser_emailAlreadyPresent() {
    UserCredential userCredential =
        new UserCredential.Builder()
            .userId(UUID.randomUUID())
            .email("test1@gmail.com")
            .hashedPassword(
                "$argon2id$v=19$m=65536,t=2,p=4$c29tZXNhbHQ$RdescudvJCsgt3ub+b+dWRWJTmaaJObG")
            .createdAt(Instant.now())
            .updatedAt(Instant.now().plusSeconds(3600))
            .build();
    RegisterUserRequest alreadyPresentUserRequest =
        new RegisterUserRequest.Builder()
            .email("test1@gmail.com")
            .password("Password@123456")
            .confirmPassword("Password@123456")
            .build();
    Mockito.when(userManagementRepository.findByEmail(alreadyPresentUserRequest.getEmail()))
        .thenReturn(Optional.of(userCredential));
    Assertions.assertThatExceptionOfType(UserAlreadyExistsException.class)
        .isThrownBy(() -> userManagementService.createAuthUser(alreadyPresentUserRequest))
        .withMessage("An account with this email already exists.");
  }

  @Test
  public void testCreateAuthUser_validEmail() {
    RegisterUserRequest validUserRequest =
        new RegisterUserRequest.Builder()
            .email("test-user@gmail.com")
            .password("Password@testUser1")
            .confirmPassword("Password@testUser1")
            .build();
    UserCredential savedUserCredential =
        new UserCredential.Builder()
            .userId(UUID.randomUUID())
            .email(validUserRequest.getEmail())
            .hashedPassword(
                "$argon2id$v=19$m=16384,t=2,p=1$uijzPz8Gdh8EIT8WD7U9qw$GjEXWiMCqTGoa/zV7USzLjjm5t+KmebjIqosXXQy2fc")
            .createdAt(Instant.now())
            .updatedAt(Instant.now().plusSeconds(3600))
            .build();
    RegisterUserResponse response =
        new RegisterUserResponse.Builder()
            .userId(savedUserCredential.getUserId())
            .email(savedUserCredential.getEmail())
            .message("Registration successful. Please log in.")
            .build();
    Mockito.when(userManagementRepository.findByEmail(validUserRequest.getEmail()))
        .thenReturn(Optional.empty());
    Mockito.when(passwordEncoder.encode(validUserRequest.getPassword()))
        .thenReturn(
            "$argon2id$v=19$m=16384,t=2,p=1$uijzPz8Gdh8EIT8WD7U9qw$GjEXWiMCqTGoa/zV7USzLjjm5t+KmebjIqosXXQy2fc");
    Mockito.when(userManagementRepository.save(ArgumentMatchers.any(UserCredential.class)))
        .thenReturn(savedUserCredential);
    Assertions.assertThat(userManagementService.createAuthUser(validUserRequest))
        .isEqualTo(response);
  }
}
