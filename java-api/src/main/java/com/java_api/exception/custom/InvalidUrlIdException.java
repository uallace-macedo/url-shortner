package com.java_api.exception.custom;

public class InvalidUrlIdException extends RuntimeException {
    public InvalidUrlIdException(String message) {
        super(message);
    }
}
