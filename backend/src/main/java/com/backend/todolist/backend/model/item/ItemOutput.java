package com.backend.todolist.backend.model.item;

import com.backend.todolist.backend.model.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Generated
@Data
@NoArgsConstructor
public class ItemOutput {

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("text")
	private String text;
	
	@JsonProperty("state")
	private String state;
	
	@JsonProperty("user")
	private User user;
	
	@JsonProperty("creationTime")
	private String creationTime;
	
	@JsonProperty("lastUpdateTime")
	private String lastUpdateTime;
}
