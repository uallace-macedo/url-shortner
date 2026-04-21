package com.java_api.exception.custom.url;

public class InvalidUrlIdException extends RuntimeException {
    public InvalidUrlIdException(String message) {
        super(message);
    }
}
