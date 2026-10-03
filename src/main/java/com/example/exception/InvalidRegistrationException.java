package com.example.exception;

public class InvalidRegistrationException extends RuntimeException {
    public InvalidRegistrationException() {
        super("Username must not be blank, and password must be at least 4 characters.");
    }
}