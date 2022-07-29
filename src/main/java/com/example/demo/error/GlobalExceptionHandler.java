package com.example.demo.error;

import com.example.demo.error.exceptions.CustomApiNotFoundException;
import com.example.demo.error.exceptions.CustomBadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @ExceptionHandler(value = {CustomBadRequestException.class})
    public ResponseEntity<ErrorResponseMessage> handleCustomBadRequestException
            (CustomBadRequestException exception, HttpServletRequest httpServletRequest) {

        ErrorResponseMessage error = new ErrorResponseMessage(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                httpServletRequest.getServletPath()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponseMessage> handleMethodArgumentNotValidException
            (MethodArgumentNotValidException exception, HttpServletRequest httpServletRequest) {

        ErrorResponseMessage error = new ErrorResponseMessage(
                HttpStatus.BAD_REQUEST.value(),
                exception.getClass().getSimpleName(),
                httpServletRequest.getServletPath());

        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Map<String, String> validationErrors = new HashMap<>();

        for (FieldError fieldError : fieldErrors) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        error.setValidationErrors(validationErrors);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorResponseMessage> handleException(
            Exception exception, HttpServletRequest httpServletRequest) {

        String message = exception.getMessage();

        if (exception instanceof DataIntegrityViolationException) {
            if (exception.getMessage().contains("users.UK_Email")) {
                message = "Email is already taken!";
            }
        }
        ErrorResponseMessage error = new ErrorResponseMessage(
                HttpStatus.BAD_REQUEST.value(),
                message,
                httpServletRequest.getServletPath());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
