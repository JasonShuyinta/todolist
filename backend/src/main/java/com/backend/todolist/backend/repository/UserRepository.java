package com.backend.todolist.backend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.backend.todolist.backend.model.user.User;

public interface UserRepository extends MongoRepository<User, String> {
	
	Optional<User> findByUsername(String username);
	

}
