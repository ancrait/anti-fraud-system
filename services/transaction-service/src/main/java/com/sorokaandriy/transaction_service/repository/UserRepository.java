package com.sorokaandriy.transaction_service.repository;

import com.sorokaandriy.transaction_service.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,String> {
}
