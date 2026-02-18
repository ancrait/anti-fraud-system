package com.sorokaandriy.user_service.repository;

import com.sorokaandriy.user_service.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {


}
