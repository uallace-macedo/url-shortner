package com.java_api.exception.custom.url;

public class InvalidUrlOwnershipException extends RuntimeException {
    public InvalidUrlOwnershipException(String message) {
        super(message);
    }
}
