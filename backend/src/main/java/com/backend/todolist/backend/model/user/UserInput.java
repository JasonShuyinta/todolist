package com.backend.todolist.backend.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Generated
@Data
@NoArgsConstructor
public class UserInput {
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("password")
	private String password;

}
