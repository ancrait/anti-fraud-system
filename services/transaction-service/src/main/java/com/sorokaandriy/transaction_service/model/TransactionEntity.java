package com.sorokaandriy.transaction_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class TransactionEntity {


    @Id
    private String id;
    private String userId;
    private double amount;
    private Long timestamp;
    @Enumerated(EnumType.STRING)
    private TransactionalStatus status;




}
