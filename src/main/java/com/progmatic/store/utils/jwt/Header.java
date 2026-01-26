package com.progmatic.store.utils.jwt;

import lombok.Data;

@Data
public class Header {
  private String alg;
  private String typ;

  private Header(Builder b) {
    this.alg = b.alg;
    this.typ = b.typ;
  }

  static class Builder {
    private String alg;
    private String typ;

    Builder alg(String alg) {
      this.alg = alg;
      return this;
    }

    Builder typ(String typ) {
      this.typ = typ;
      return this;
    }

    Header build() {
      return new Header(this);
    }
  }
}
