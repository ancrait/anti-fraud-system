package com.sorokaandriy.transaction_service.exception;

public class TransactionLimitException extends RuntimeException {
    public TransactionLimitException(String message) {
        super(message);
    }
}
