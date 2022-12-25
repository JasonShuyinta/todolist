package com.backend.todolist.backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;


@Configuration
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf().disable()
				.cors().disable()
//				.cors().configurationSource(request -> {
//					var cors = new CorsConfiguration();
//					cors.addAllowedOrigin("*");
//					cors.setAllowedOrigins(List.of("*"));
//					cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
//					cors.setAllowedHeaders(List.of("*"));
//					cors.setExposedHeaders(List.of("*"));
//					return cors;
//				})
//				.and()
				.authorizeRequests()
				.antMatchers("/**").permitAll()
				.and()
				.authorizeRequests(auth -> auth.anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
