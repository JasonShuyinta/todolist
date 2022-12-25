package com.backend.todolist.backend.service;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.backend.todolist.backend.config.AppConfig;
import com.backend.todolist.backend.model.user.User;
import com.backend.todolist.backend.repository.UserRepository;
import com.backend.todolist.backend.utils.JWTObject;
import com.backend.todolist.backend.utils.exceptions.ExpiredTokenException;
import com.backend.todolist.backend.utils.exceptions.InvalidTokenException;
import com.backend.todolist.backend.utils.exceptions.PasswordDontMatchException;
import com.backend.todolist.backend.utils.exceptions.UsernameAlreadyExistsException;
import com.google.gson.Gson;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import lombok.extern.slf4j.Slf4j;

@Service
@Component
@Slf4j
public class AuthenticationService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AppConfig appConfig;
	
	@Autowired
	UserService userService;

	public User saveUser(User user) {
		log.info("START saveUser {} ", this.getClass().getSimpleName());
		try {
			if(usernameAlreadyExists(user.getUsername())) throw new UsernameAlreadyExistsException();
			else {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				user.setSubscriptionDate(LocalDateTime.now());
				return userRepository.save(user);
			}
		} catch(UsernameAlreadyExistsException e) {
			log.error("UsernameAlreadyExistsException {} ", this.getClass().getSimpleName());
			throw new UsernameAlreadyExistsException(user.getUsername());
		}
		
	}
	
	public boolean usernameAlreadyExists(String username) {
		log.info("START usernameAlreadyExists {} ", this.getClass().getSimpleName());
		Boolean usernameExists = Boolean.FALSE;
		if(!userRepository.findByUsername(username).isEmpty()) usernameExists = Boolean.TRUE;
		log.info("END usernameAlreadyExists {} " , this.getClass().getSimpleName());
		return usernameExists;
		
	}
	
	public User login(User user) {
		log.info("START login {} ", this.getClass().getSimpleName());
		if(user.getUsername() != null && user.getPassword() != null) {
			Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
			log.info("User is {} and {} ", optionalUser.get().getId(), optionalUser.get().getUsername());
			if(!optionalUser.isEmpty()) {
				if(passwordEncoder.matches(user.getPassword(), optionalUser.get().getPassword())) {
					log.info("Login successful");
					return optionalUser.get();
				} else throw new PasswordDontMatchException("Password don't match");
			}
		}
		log.info("END login {} " , this.getClass().getSimpleName());
		return null;
	}
	
	public String generateToken(User user) {
		Algorithm algorithm = Algorithm.HMAC256(appConfig.getSecretKey().getBytes());
		log.info("USer id is {} ", user.getId());
		return JWT.create().withSubject(user.getId())
				.withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)).sign(algorithm);
	}
	
	public User getUserByToken(String token) throws Exception {
		try {
			SignatureAlgorithm sa = HS256;
			SecretKeySpec secretKeySpec = new SecretKeySpec(appConfig.getSecretKey().getBytes(), sa.getJcaName());
			Base64.Decoder decoder = Base64.getUrlDecoder();
			if (token != null && token.startsWith("Bearer ")) {
				String[] tokens = token.split(" ");
				String[] access = tokens[1].split("\\.");
				String tokenWithoutSignature = access[0] + "." + access[1];
				DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);
				if (!validator.isValid(tokenWithoutSignature, access[2])) {
					throw new InvalidTokenException("Could not verify token integrity");
				} else {
					Gson g = new Gson();
					String accessPayload = new String(decoder.decode(access[1]));
					JWTObject jwtObject = g.fromJson(accessPayload, JWTObject.class);
					Date expirationDate = new Date(Long.parseLong(jwtObject.getExp()) * 1000);
					Date today = new Date(System.currentTimeMillis());
					if (today.compareTo(expirationDate) > 0) {
						throw new ExpiredTokenException("Token is expired");
					} else {
						log.info(jwtObject.toString());
						return userService.getUserById(jwtObject.getSub());
					}
				}
			} else
				throw new InvalidTokenException("Invalid token");
		} catch (InvalidTokenException e) {
			log.error(e.toString());
			throw new InvalidTokenException(e.getMessage());
		} catch (ExpiredTokenException ex) {
			log.error(ex.toString());
			throw new ExpiredTokenException(ex.getMessage());
		} catch (Exception e) {
			log.error(e + this.getClass().getSimpleName());
			throw new Exception(e.getMessage());
		}
	}
	
}
