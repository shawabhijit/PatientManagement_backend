package com.pm.billingservice.exceptions;

public class InvoiceNotExitsException extends RuntimeException {
    public InvoiceNotExitsException(String message) {
        super(message);
    }
}
