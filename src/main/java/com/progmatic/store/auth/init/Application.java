package com.progmatic.store.auth.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.progmatic.store.auth"})
@EnableJpaRepositories(basePackages = {"com.progmatic.store.auth"})
@EntityScan(basePackages = {"com.progmatic.store.auth.entity"})
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class);
  }
}
