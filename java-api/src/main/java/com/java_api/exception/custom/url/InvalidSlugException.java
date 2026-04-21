package com.java_api.exception.custom.url;

public class InvalidSlugException extends RuntimeException {
    public InvalidSlugException(String message) {
        super(message);
    }
}
