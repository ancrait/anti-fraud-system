package com.sorokaandriy.transaction_service.exception;

public class NotEnoughBalance extends RuntimeException {
    public NotEnoughBalance(String message) {
        super(message);
    }
}
