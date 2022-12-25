package com.backend.todolist.backend.model.item;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.backend.todolist.backend.model.user.User;

import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Generated
@Data
@Document
@NoArgsConstructor
public class Item {
	
	@Id
	private String id;
	
	private String text;
	
	private String state;
	
	private User user;
	
	private LocalDateTime creationTime;
	
	private LocalDateTime lastUpdateTime;
	

}
