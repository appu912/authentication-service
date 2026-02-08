package com.progmatic.auth.service;

import com.progmatic.auth.entity.UserCredential;
import com.progmatic.auth.exception.UserAlreadyExistsException;
import com.progmatic.auth.repository.UserManagementRepository;
import com.progmatic.auth.utils.RegisterUserRequest;
import com.progmatic.auth.utils.RegisterUserResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserManagementServiceImpl implements UserManagementService {
  private final UserManagementRepository userManagementRepository;
  private final PasswordEncoder passwordEncoder;
  private final Validator validator;

  public UserManagementServiceImpl(
      UserManagementRepository userManagementRepository,
      PasswordEncoder passwordEncoder,
      Validator validator) {
    this.userManagementRepository = userManagementRepository;
    this.passwordEncoder = passwordEncoder;
    this.validator = validator;
  }

  @Override
  public RegisterUserResponse createAuthUser(@Valid RegisterUserRequest registerUserRequest)
      throws UserAlreadyExistsException, ConstraintViolationException {
    log.info("User registration in progress.");
    Set<ConstraintViolation<RegisterUserRequest>> violations =
        validator.validate(registerUserRequest);
    if (!violations.isEmpty()) {
      log.debug("Constraints violated at service layer {}", violations);
      throw new ConstraintViolationException(
          "Invalid request body. Constraints have been violated.", violations);
    }
    boolean isAuthUserPresent =
        userManagementRepository.findByEmail(registerUserRequest.getEmail()).isPresent();
    if (isAuthUserPresent) {
      log.debug("User with email id '{}' already exists", registerUserRequest.getEmail());
      throw new UserAlreadyExistsException("An account with this email already exists.");
    }
    String password = registerUserRequest.getPassword();
    String hashedPassword = passwordEncoder.encode(password);
    UserCredential userCredential =
        new UserCredential.Builder()
            .userId(UUID.randomUUID())
            .email(registerUserRequest.getEmail())
            .hashedPassword(hashedPassword)
            .build();
    UserCredential savedUserCredential = userManagementRepository.save(userCredential);
    log.info("User successfully created.");
    return new RegisterUserResponse.Builder()
        .userId(savedUserCredential.getUserId())
        .email(savedUserCredential.getEmail())
        .message("Registration successful. Please log in.")
        .build();
  }
}
