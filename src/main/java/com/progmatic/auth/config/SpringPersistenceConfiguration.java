package com.progmatic.auth.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableJpaRepositories(basePackages = {"com.progmatic.auth.repository"})
@EntityScan(basePackages = {"com.progmatic.auth.entity"})
public class SpringPersistenceConfiguration {}
