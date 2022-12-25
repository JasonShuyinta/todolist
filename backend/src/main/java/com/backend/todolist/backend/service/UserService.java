package com.backend.todolist.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.backend.todolist.backend.model.user.User;
import com.backend.todolist.backend.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Component
@Slf4j
public class UserService {
	
	@Autowired
	UserRepository userRepository;

	
	public List<User> getAllUsers() {
		log.info("START getAllUsers {} ", this.getClass().getSimpleName());
		return userRepository.findAll();		
	}
	
	public User getUserById(String id) {
		log.info("START getUserById {} ", this.getClass().getSimpleName());
		Optional<User> user = userRepository.findById(id);
		if(user.get() != null) return user.get();
		else return null;
	}
	
}
