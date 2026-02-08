package com.progmatic.auth.utils;

import java.time.Instant;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValidationErrorResponse {
  private Map<String, String> messages;
  private String code;
  private Instant timestamptz;

  private ValidationErrorResponse(Builder b) {
    this.messages = b.messages;
    this.code = b.code;
    this.timestamptz = b.timestamptz;
  }

  public static class Builder {
    private Map<String, String> messages;
    private String code;
    private Instant timestamptz;

    public Builder messages(Map<String, String> messages) {
      this.messages = messages;
      return this;
    }

    public Builder code(String code) {
      this.code = code;
      return this;
    }

    public Builder timestamptz(Instant timestamptz) {
      this.timestamptz = timestamptz;
      return this;
    }

    public ValidationErrorResponse build() {
      return new ValidationErrorResponse(this);
    }
  }
}
