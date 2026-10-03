package com.example.exception;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException() {
        super("Invalid username or password.");
    }
}