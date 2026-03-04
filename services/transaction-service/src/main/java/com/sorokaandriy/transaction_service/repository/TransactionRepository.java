package com.sorokaandriy.transaction_service.repository;

import com.sorokaandriy.transaction_service.model.TransactionEntity;
import com.sorokaandriy.transaction_service.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity,String> {

    List<TransactionEntity> findAllByUserId_Id(String userId);

    Optional<TransactionEntity> findFirstByUserId_IdOrderByTimestampDesc(String userIdId);
}
