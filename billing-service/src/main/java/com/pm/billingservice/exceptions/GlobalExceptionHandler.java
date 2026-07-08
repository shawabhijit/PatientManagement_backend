package com.pm.billingservice.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountExitsException.class)
    public ResponseEntity<Map<String , String>> handleAccountAlreadyExitsException(
            AccountExitsException ex
    ) {
        log.warn("Billing account already exits with this patient Id. {}", ex.getMessage());
        Map<String , String> errors = new HashMap<>();
        errors.put("message", "Billing account already exits with this patient id : "+ ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
