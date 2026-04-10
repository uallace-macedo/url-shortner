package com.java_api.exception.custom;

public class UserEmailAlreadyTaken extends RuntimeException {
    public UserEmailAlreadyTaken(String message) {
        super(message);
    }
}
