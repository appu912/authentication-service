package com.progmatic.store.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequest {
  private String email;
  private String password;
}
