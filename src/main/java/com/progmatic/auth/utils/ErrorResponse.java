package com.progmatic.auth.utils;

import java.time.Instant;
import lombok.Data;

@Data
public class ErrorResponse {
  private String message;
  private String code;
  private Instant timestamptz;

  private ErrorResponse(Builder b) {
    this.message = b.message;
    this.code = b.code;
    this.timestamptz = b.timestamptz;
  }

  public static class Builder {
    private String message;
    private String code;
    private Instant timestamptz;

    public Builder message(String message) {
      this.message = message;
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

    public ErrorResponse build() {
      return new ErrorResponse(this);
    }
  }
}
