package com.backend.todolist.backend.utils.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;

@Generated
@EqualsAndHashCode(callSuper = true)
@Data
public class InvalidTokenException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 861868928265032695L;

	public InvalidTokenException() {
        super();
    }

    public InvalidTokenException(String msg) {
        super(msg);
    }
}
