package com.java_api.exception.custom.user;

public class UserEmailAlreadyTakenException extends RuntimeException {
    public UserEmailAlreadyTakenException(String message) {
        super(message);
    }
}
