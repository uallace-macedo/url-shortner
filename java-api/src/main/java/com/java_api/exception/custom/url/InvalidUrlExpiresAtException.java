package com.java_api.exception.custom.url;

public class InvalidUrlExpiresAtException extends RuntimeException {
    public InvalidUrlExpiresAtException(String message) {
        super(message);
    }
}
