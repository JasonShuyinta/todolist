package com.backend.todolist.backend.controller;

import static com.backend.todolist.backend.utils.Constants.END;
import static com.backend.todolist.backend.utils.Constants.EXCEPTION;
import static com.backend.todolist.backend.utils.Constants.START;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.backend.todolist.backend.model.user.User;
import com.backend.todolist.backend.model.user.UserConverter;
import com.backend.todolist.backend.model.user.UserOutput;
import com.backend.todolist.backend.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@CrossOrigin("*")
@Slf4j
@RequestMapping(ParentController.BASE_URI + "/user")
public class UserController extends ParentController {

	@Autowired
	UserService userService;
	@Autowired
	UserConverter userConverter;

	@GetMapping("/{userId}")
	public ResponseEntity<UserOutput> findUserById(@PathVariable String userId) {
		log.info(START + " findUserById " + this.getClass().getSimpleName());
		try {
			User user = userService.getUserById(userId);
			UserOutput output = userConverter.entityToOutput(user);
			log.info(END + " findUserById " + this.getClass().getSimpleName());
			return ResponseEntity.ok(output);
		} catch (Exception e) {
			log.error(EXCEPTION , e, this.getClass().getSimpleName());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
