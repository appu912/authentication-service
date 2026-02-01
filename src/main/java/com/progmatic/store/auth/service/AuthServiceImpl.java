package com.progmatic.store.auth.service;

import com.progmatic.store.auth.entity.AuthUserCredentials;
import com.progmatic.store.auth.repository.AuthUserRepository;
import com.progmatic.store.auth.utils.UserData;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
  private final AuthUserRepository authUserRepository;
  private final AuthUserCredentials.Builder authUserCredentialsBuilder;

  public AuthServiceImpl(
      AuthUserRepository authUserRepository,
      AuthUserCredentials.Builder authUserCredentialsBuilder) {
    this.authUserRepository = authUserRepository;
    this.authUserCredentialsBuilder = authUserCredentialsBuilder;
  }

  @Override
  public AuthUserCredentials createAuthUser(UserData userData) {
    AuthUserCredentials authUserCredentials =
        authUserCredentialsBuilder
            .userId(UUID.randomUUID())
            .email(userData.email())
            .hashedPassword(userData.password())
            .build();
    return authUserRepository.save(authUserCredentials);
  }
}
