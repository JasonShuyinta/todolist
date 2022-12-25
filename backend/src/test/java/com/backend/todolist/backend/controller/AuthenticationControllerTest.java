package com.backend.todolist.backend.controller;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.backend.todolist.backend.BackendApplicationTests;
import com.backend.todolist.backend.model.user.User;
import com.backend.todolist.backend.model.user.UserInput;
import com.backend.todolist.backend.repository.UserRepository;

public class AuthenticationControllerTest extends BackendApplicationTests{
	
	@Autowired
	UserRepository userRepository;
	
	@Test
	void saveUser() throws Exception {
		User input = new User();
		input.setUsername(getRandomString());
		input.setPassword("password");
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.post(URL_PREFIX+AUTH_ENDPOINT+"/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input)))
				.andReturn();
		Assertions.assertEquals(200, result.getResponse().getStatus());		
	}
	
	
	@Test
	void saveUserUsernameAlreadyExists() throws Exception {
		User input = new User();
		input.setUsername("jason");
		input.setPassword("password");
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.post(URL_PREFIX+AUTH_ENDPOINT+"/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input)))
				.andReturn();
		Assertions.assertEquals(400, result.getResponse().getStatus());	
	}
	
	@Test
	void login() throws Exception {
		Optional<User> op = userRepository.findByUsername(USERNAME);
		User input = new User();
		input.setUsername(op.get().getUsername());
		input.setPassword("password");
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.post(URL_PREFIX+AUTH_ENDPOINT+"/login")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input)))
				.andReturn();
		Assertions.assertEquals(200, result.getResponse().getStatus());
	}
	
	@Test
	void loginFailed() throws Exception {
		UserInput input = new UserInput();
		input.setUsername(null);
		input.setPassword("password");
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.post(URL_PREFIX+AUTH_ENDPOINT+"/login")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input)))
				.andReturn();
		Assertions.assertEquals(400, result.getResponse().getStatus());
	}
}
