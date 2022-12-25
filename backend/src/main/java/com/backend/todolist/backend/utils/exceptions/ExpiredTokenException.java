package com.backend.todolist.backend.utils.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;

@Generated
@EqualsAndHashCode(callSuper = true)
@Data
public class ExpiredTokenException extends Exception{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1739061007512977578L;

	public ExpiredTokenException() {
        super();
    }

    public ExpiredTokenException(String msg) {
        super(msg);
    }
}
