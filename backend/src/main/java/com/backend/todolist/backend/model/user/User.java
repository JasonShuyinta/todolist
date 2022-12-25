package com.backend.todolist.backend.model.user;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Generated
@Data
@Document
@NoArgsConstructor
public class User {
	
	@Id
	private String id;
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("password")
	private String password;
	
	private LocalDateTime subscriptionDate;
	
}
