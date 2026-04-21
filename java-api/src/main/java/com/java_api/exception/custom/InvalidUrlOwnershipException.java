package com.java_api.exception.custom;

public class InvalidUrlOwnershipException extends RuntimeException {
    public InvalidUrlOwnershipException(String message) {
        super(message);
    }
}
