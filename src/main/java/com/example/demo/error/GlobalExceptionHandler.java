package com.example.demo.error;

import com.example.demo.error.exceptions.CustomApiNotFoundException;
import com.example.demo.error.exceptions.SkuCannotBeUpdatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {CustomApiNotFoundException.class})
    public ResponseEntity<ErrorResponseMessage> handleCustomApiNotFoundException
            (CustomApiNotFoundException exception, HttpServletRequest httpServletRequest) {

        ErrorResponseMessage error = new ErrorResponseMessage(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                httpServletRequest.getServletPath()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {SkuCannotBeUpdatedException.class})
    public ResponseEntity<ErrorResponseMessage> handleSkuCannotBeUpdatedException
            (SkuCannotBeUpdatedException exception, HttpServletRequest httpServletRequest) {

        ErrorResponseMessage error = new ErrorResponseMessage(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                httpServletRequest.getServletPath()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorResponseMessage> handleException(Exception exception) {

        ErrorResponseMessage error = new ErrorResponseMessage(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                "N/A");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
