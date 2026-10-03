package com.example.exception;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException() {
        super("An account with that username already exists.");
    }
}