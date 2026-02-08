package com.progmatic.auth.service;

import com.progmatic.auth.utils.RegisterUserRequest;
import com.progmatic.auth.utils.RegisterUserResponse;

public interface UserManagementService {
  RegisterUserResponse createAuthUser(RegisterUserRequest registerUserRequest);
}
