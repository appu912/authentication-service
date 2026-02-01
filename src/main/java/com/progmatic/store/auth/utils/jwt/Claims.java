package com.progmatic.store.auth.utils.jwt;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"sub", "name", "admin", "iat"})
public class Claims {
  private String sub;
  private String name;
  private Boolean admin;
  private Long iat;

  private Claims(Builder b) {
    this.sub = b.sub;
    this.name = b.name;
    this.admin = b.admin;
    this.iat = b.iat;
  }

  static class Builder {
    private String sub;
    private String name;
    private Boolean admin;
    private Long iat;

    Builder sub(String sub) {
      this.sub = sub;
      return this;
    }

    Builder name(String name) {
      this.name = name;
      return this;
    }

    Builder admin(Boolean admin) {
      this.admin = admin;
      return this;
    }

    Builder iat(Long iat) {
      this.iat = iat;
      return this;
    }

    Claims build() {
      return new Claims(this);
    }
  }
}
