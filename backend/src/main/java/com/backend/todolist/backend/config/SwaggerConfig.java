package com.backend.todolist.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Profile({"!prod && dev"})
@Configuration
public class SwaggerConfig {

}
