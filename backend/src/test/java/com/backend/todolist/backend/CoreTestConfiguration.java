package com.backend.todolist.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebMvc
@ComponentScan("com.backend.todolist")
public class CoreTestConfiguration {

	@Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
