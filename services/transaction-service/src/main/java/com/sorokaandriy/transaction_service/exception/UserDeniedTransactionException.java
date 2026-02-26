package com.sorokaandriy.transaction_service.exception;

public class UserDeniedTransactionException extends RuntimeException {
    public UserDeniedTransactionException(String message) {
        super(message);
    }
}
