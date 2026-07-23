package com.meichel.user_service.expection;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("User with id: " + id + " not found.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}