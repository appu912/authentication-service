package com.progmatic.store.auth.service;

import com.progmatic.store.auth.entity.AuthUserCredentials;
import com.progmatic.store.auth.utils.UserData;

public interface AuthService {
  AuthUserCredentials createAuthUser(UserData userData);
}
