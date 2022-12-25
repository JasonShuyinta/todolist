package com.backend.todolist.backend.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Generated
@Data
@NoArgsConstructor
public class UserOutput {
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("subscriptionDate")
	private String subscriptionDate;

	@JsonProperty("accessToken")
	private String accessToken;
}
