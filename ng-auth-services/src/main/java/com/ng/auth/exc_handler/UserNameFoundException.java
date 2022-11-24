package com.ng.auth.exc_handler;

public class UserNameFoundException extends RuntimeException {

    public UserNameFoundException(String message) {
        super(message);
    }
}
