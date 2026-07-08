package com.pm.billingservice.exceptions;

public class AccountExitsException extends RuntimeException{
    public AccountExitsException(String message) {
        super(message);
    }
}
