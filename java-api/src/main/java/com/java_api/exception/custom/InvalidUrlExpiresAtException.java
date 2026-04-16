package com.java_api.exception.custom;

public class InvalidUrlExpiresAtException extends RuntimeException {
    public InvalidUrlExpiresAtException(String message) {
        super(message);
    }
}
