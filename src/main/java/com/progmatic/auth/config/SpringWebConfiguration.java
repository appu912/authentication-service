package com.progmatic.auth.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@ComponentScan(basePackages = {"com.progmatic.auth.controller"})
public class SpringWebConfiguration {}
