package com.backend.todolist.backend.controller;

import static com.backend.todolist.backend.utils.Constants.END;
import static com.backend.todolist.backend.utils.Constants.EXCEPTION;
import static com.backend.todolist.backend.utils.Constants.START;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.backend.todolist.backend.model.user.User;
import com.backend.todolist.backend.model.user.UserConverter;
import com.backend.todolist.backend.model.user.UserInput;
import com.backend.todolist.backend.model.user.UserOutput;
import com.backend.todolist.backend.service.AuthenticationService;
import com.backend.todolist.backend.utils.exceptions.PasswordDontMatchException;
import com.backend.todolist.backend.utils.exceptions.UsernameAlreadyExistsException;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping(ParentController.BASE_URI + "/auth")
@CrossOrigin("*")
public class AuthenticationController extends ParentController {
	
	@Autowired
	AuthenticationService authService;
	@Autowired
	UserConverter userConverter;
	
	@PostMapping("/signup")
	public ResponseEntity<UserOutput> saveUser(@RequestBody UserInput input) {
		log.info(START +" saveUser " + this.getClass().getSimpleName());
		try {
			User user = authService.saveUser(userConverter.inputToEntity(input));
			if(user != null) {
				String token = authService.generateToken(user);
				log.info(END + " saveUser " + this.getClass().getSimpleName());
				UserOutput output = userConverter.entityToOutput(user);
				output.setAccessToken(token);
				return ResponseEntity.ok().body(output);
			} else return ResponseEntity.internalServerError().build();
		} catch (UsernameAlreadyExistsException e) {
			log.error(EXCEPTION, e, this.getClass().getSimpleName());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} catch (Exception e) {
			log.error(EXCEPTION, e, this.getClass().getSimpleName());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/login")
	public ResponseEntity<UserOutput> login(@RequestBody UserInput input) {
		log.info(START +" login " + this.getClass().getSimpleName());
		try {
			User user = authService.login(userConverter.inputToEntity(input));
			if (user != null) {
				String token = authService.generateToken(user);
				log.info(END + " login " + this.getClass().getSimpleName());
				UserOutput output = userConverter.entityToOutput(user);
				output.setAccessToken(token);
				return ResponseEntity.ok().body(output);
			} else return ResponseEntity.badRequest().build();
		} catch(PasswordDontMatchException e) {
		    log.error(EXCEPTION , e, this.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (Exception e) {
			log.error(EXCEPTION, e, this.getClass().getSimpleName());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/getUserFromToken")
	public ResponseEntity<User> getUserFromToken(@RequestHeader("Authorization") String token) {
		log.info(START +" getUserFromToken " + this.getClass().getSimpleName());
		try {
			User user = authService.getUserByToken(token);
			log.info(END + " getUserFromToken " + this.getClass().getSimpleName());
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			log.error(EXCEPTION, e, this.getClass().getSimpleName());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
