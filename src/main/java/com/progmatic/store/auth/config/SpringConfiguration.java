package com.progmatic.store.auth.config;

import com.progmatic.store.auth.entity.AuthUserCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class SpringConfiguration {

  @Bean
  public AuthUserCredentials.Builder authUserCredentialsBuilder() {
    return new AuthUserCredentials.Builder();
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}
