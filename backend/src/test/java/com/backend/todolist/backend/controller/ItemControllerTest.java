package com.backend.todolist.backend.controller;

import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.backend.todolist.backend.BackendApplicationTests;
import com.backend.todolist.backend.model.item.Item;
import com.backend.todolist.backend.model.item.ItemConverter;
import com.backend.todolist.backend.model.item.ItemInput;
import com.backend.todolist.backend.model.user.User;
import com.backend.todolist.backend.repository.ItemRepository;
import com.backend.todolist.backend.repository.UserRepository;
import com.backend.todolist.backend.service.AuthenticationService;
import com.jayway.jsonpath.JsonPath;

public class ItemControllerTest extends BackendApplicationTests {
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ItemConverter itemConverter;
	
	@MockBean
	AuthenticationService authService;
	
	@Test
	void saveItemTest() throws Exception {
		Optional<User> op = userRepository.findByUsername(USERNAME);
		
		Item input = new Item();
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.post(URL_PREFIX+ITEM_ENDPOINT+"/saveItem")
				.header(AUTHORIZATION, "token")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input)))
				.andReturn();
		when(authService.getUserByToken(ArgumentMatchers.any())).thenReturn(op.get());
		Assertions.assertEquals(200, result.getResponse().getStatus());
	}
	
	@Test
	void getUsersItem() throws Exception {
		Optional<User> op = userRepository.findByUsername(USERNAME);
		
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.get(URL_PREFIX+ITEM_ENDPOINT+"/getUsersItem")
				.header(AUTHORIZATION, "token"))
				.andReturn();
		when(authService.getUserByToken(ArgumentMatchers.any())).thenReturn(op.get());
		Assertions.assertEquals(200, result.getResponse().getStatus());
	}
	
	@Test
	void nextAndPreviousStep() throws Exception {
		
		Optional<User> op = userRepository.findByUsername(USERNAME);
		
		Item newItem = new Item();
		MvcResult newItemResult = mockMvc.perform(MockMvcRequestBuilders
				.post(URL_PREFIX+ITEM_ENDPOINT+"/saveItem")
				.header(AUTHORIZATION, "token")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newItem)))
				.andReturn();
		when(authService.getUserByToken(ArgumentMatchers.any())).thenReturn(op.get());
		
		String id = JsonPath.read(newItemResult.getResponse().getContentAsString(), "$.id");
		
		ItemInput input = new ItemInput();
		input.setId(id);
		MvcResult nextResult = mockMvc.perform(MockMvcRequestBuilders
				.post(URL_PREFIX+ITEM_ENDPOINT+"/nextStep")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input)))
				.andReturn();
		Assertions.assertEquals(200, nextResult.getResponse().getStatus());
		
		
		MvcResult previousResult = mockMvc.perform(MockMvcRequestBuilders
				.post(URL_PREFIX+ITEM_ENDPOINT+"/previousStep")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input)))
				.andReturn();
		Assertions.assertEquals(200, previousResult.getResponse().getStatus());

	}
	
	@Test
	void editText() throws Exception {
		ItemInput input = new ItemInput();
		input.setId(ITEM_ID);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.put(URL_PREFIX+ITEM_ENDPOINT+"/editText")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input)))
				.andReturn();
		Assertions.assertEquals(200, result.getResponse().getStatus());
	}
	
	@Test
	void deleteItem() throws Exception {
		Optional<User> op = userRepository.findByUsername(USERNAME);
		
		Item newItem = new Item();
		MvcResult newItemResult = mockMvc.perform(MockMvcRequestBuilders
				.post(URL_PREFIX+ITEM_ENDPOINT+"/saveItem")
				.header(AUTHORIZATION, "token")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newItem)))
				.andReturn();
		when(authService.getUserByToken(ArgumentMatchers.any())).thenReturn(op.get());
		
		String itemId = JsonPath.read(newItemResult.getResponse().getContentAsString(), "$.id");

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.delete(URL_PREFIX+ITEM_ENDPOINT+"/{itemId}", itemId))
				.andReturn();
		Assertions.assertEquals(200, result.getResponse().getStatus());

	}
	

}
