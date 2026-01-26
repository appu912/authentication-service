package com.progmatic.store.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.progmatic.store"})
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class);
  }
}
