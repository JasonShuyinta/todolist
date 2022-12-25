package com.backend.todolist.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Configuration
@Component
public class AppConfig {
	
	@Value("${spring.app.secretKey}")
	public String secretKey;
	
	public String getSecretKey() {
		return secretKey;
	}
}
