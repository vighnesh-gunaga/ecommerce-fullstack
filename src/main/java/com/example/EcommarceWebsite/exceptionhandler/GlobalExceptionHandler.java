package com.example.EcommarceWebsite.exceptionhandler;


import com.example.EcommarceWebsite.exception.CategoryNotFoundException;
import com.example.EcommarceWebsite.exception.EmailAlreadyExistsException;
import com.example.EcommarceWebsite.exception.InvalidCredentialException;
import com.example.EcommarceWebsite.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handelCategoryNotFoundEception(CategoryNotFoundException exception)
    {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handelProductNotFoundException(ProductNotFoundException exception)
    {
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handelEmailAlreadyEistException(EmailAlreadyExistsException exception)
    {
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        ));

        return ResponseEntity
                .badRequest()
                .body(errors);
    }
}
