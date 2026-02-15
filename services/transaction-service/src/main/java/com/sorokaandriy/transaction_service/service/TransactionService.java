package com.sorokaandriy.transaction_service.service;

import com.sorokaandriy.transaction_service.dto.TransactionRequest;
import com.sorokaandriy.transaction_service.model.TransactionEntity;
import com.sorokaandriy.transaction_service.repository.TransactionRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;
    private final TransactionProducer transactionProducer;

    public TransactionService(TransactionRepository repository, TransactionMapper mapper, TransactionProducer transactionProducer) {
        this.repository = repository;
        this.mapper = mapper;
        this.transactionProducer = transactionProducer;
    }

    public String createTransaction(@Valid TransactionRequest request) {
        TransactionEntity transaction = mapper.fromTransactionRequestToTransaction(request);
        repository.save(transaction);
        transactionProducer.sendTransactionEvent(mapper.fromTransactionToEvent(transaction));
        return String.format("Transaction wit id %s is pending", transaction.getId());

    }
}
