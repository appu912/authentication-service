package com.progmatic.store.auth.controller;

import com.progmatic.store.auth.utils.RegisterRequest;
import com.progmatic.store.auth.utils.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

  @Autowired private JwtUtils jwtUtils;

  @RequestMapping(path = "/register", method = RequestMethod.POST)
  public ResponseEntity<String> createUser(@RequestBody RegisterRequest request) {
    return ResponseEntity.status(HttpStatusCode.valueOf(201))
        .body(jwtUtils.getJwtToken(request.getEmail()));
  }
}
