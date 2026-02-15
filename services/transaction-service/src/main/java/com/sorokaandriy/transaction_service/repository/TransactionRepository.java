package com.sorokaandriy.transaction_service.repository;

import com.sorokaandriy.transaction_service.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity,String> {
}
