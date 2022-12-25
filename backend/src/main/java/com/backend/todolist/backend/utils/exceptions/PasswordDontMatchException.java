package com.backend.todolist.backend.utils.exceptions;

public class PasswordDontMatchException extends RuntimeException {
    
    public PasswordDontMatchException(String message) { super(message); }
    public PasswordDontMatchException() {super();}

}
