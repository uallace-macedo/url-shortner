package com.java_api.exception.custom.url;

public class UrlForbiddenException extends RuntimeException {
    public UrlForbiddenException(String message) {
        super(message);
    }
}
