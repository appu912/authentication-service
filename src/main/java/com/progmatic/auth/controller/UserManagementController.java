package com.progmatic.auth.controller;

import com.progmatic.auth.service.UserManagementService;
import com.progmatic.auth.utils.RegisterUserRequest;
import com.progmatic.auth.utils.RegisterUserResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/auth/users")
public class UserManagementController {

  private final UserManagementService userManagementService;

  public UserManagementController(UserManagementService userManagementService) {
    this.userManagementService = userManagementService;
  }

  @RequestMapping(path = "/register", method = RequestMethod.POST)
  public ResponseEntity<RegisterUserResponse> createUser(
      @RequestBody @Valid RegisterUserRequest registerUserRequest) {
    log.info("Processing user registration request.");
    RegisterUserResponse responseBody = userManagementService.createAuthUser(registerUserRequest);
    log.info("User successfully registered.");
    return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(responseBody);
  }
}
