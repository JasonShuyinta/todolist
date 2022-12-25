package com.backend.todolist.backend.controller;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.backend.todolist.backend.BackendApplicationTests;
import com.backend.todolist.backend.repository.UserRepository;

public class UserControllerTest extends BackendApplicationTests{
	
	@Autowired
	UserRepository userRepository;
	
	@Test
	void findUserById() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.get(URL_PREFIX+"/user/{userId}", USER_ID))
				.andReturn();
		Assertions.assertEquals(200, result.getResponse().getStatus());
	}
}
