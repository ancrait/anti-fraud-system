package com.sorokaandriy.transaction_service.exception;

public class TransactionalNotFoundException extends RuntimeException {
    public TransactionalNotFoundException(String message) {
        super(message);
    }
}
