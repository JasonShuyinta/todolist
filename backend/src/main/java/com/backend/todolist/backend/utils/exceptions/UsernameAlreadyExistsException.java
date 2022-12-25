package com.backend.todolist.backend.utils.exceptions;

import lombok.Generated;

@Generated
public class UsernameAlreadyExistsException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7866067363897246031L;
	
	public UsernameAlreadyExistsException(String message) { super(message); }
	public UsernameAlreadyExistsException() {super();}

}
