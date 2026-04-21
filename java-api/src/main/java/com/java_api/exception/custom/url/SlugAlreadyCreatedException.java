package com.java_api.exception.custom.url;

public class SlugAlreadyCreatedException extends RuntimeException {
    public SlugAlreadyCreatedException(String message) {
        super(message);
    }
}
