package com.progmatic.store.auth.controller;

import com.progmatic.store.auth.entity.AuthUserCredentials;
import com.progmatic.store.auth.service.AuthService;
import com.progmatic.store.auth.utils.UserData;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

  private final AuthService authService;

  public AuthenticationController(AuthService authService) {
    this.authService = authService;
  }

  @RequestMapping(path = "/register", method = RequestMethod.POST)
  public ResponseEntity<AuthUserCredentials> createUser(@RequestBody UserData userData) {
    AuthUserCredentials credentials = authService.createAuthUser(userData);
    return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(credentials);
  }
}
