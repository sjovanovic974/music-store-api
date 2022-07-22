package com.example.demo.error.exceptions;

public class CustomApiNotFoundException extends RuntimeException {

    public CustomApiNotFoundException(String message) {
        super(message);
    }

}
