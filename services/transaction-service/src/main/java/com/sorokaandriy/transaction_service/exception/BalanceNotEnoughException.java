package com.sorokaandriy.transaction_service.exception;

public class BalanceNotEnoughException extends RuntimeException {
    public BalanceNotEnoughException(String message) {
        super(message);
    }
}
