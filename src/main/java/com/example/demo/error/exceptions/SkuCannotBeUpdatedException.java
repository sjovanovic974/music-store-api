package com.example.demo.error.exceptions;

public class SkuCannotBeUpdatedException extends RuntimeException {
    public SkuCannotBeUpdatedException(String message) {
        super(message);
    }
}
