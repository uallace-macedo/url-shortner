package com.java_api.exception.custom;

public class SlugAlreadyExistsException extends RuntimeException {
    public SlugAlreadyExistsException(String message) {
        super(message);
    }
}
