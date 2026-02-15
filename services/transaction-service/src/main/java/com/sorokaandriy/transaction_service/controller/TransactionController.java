package com.sorokaandriy.transaction_service.controller;


import com.sorokaandriy.transaction_service.TransactionServiceApplication;
import com.sorokaandriy.transaction_service.dto.TransactionRequest;
import com.sorokaandriy.transaction_service.service.TransactionService;
import jakarta.transaction.Transaction;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> createTransaction(
            @RequestBody @Valid TransactionRequest request
            ){
        return ResponseEntity.ok(service.createTransaction(request));
    }
}
