package com.sorokaandriy.user_service.service;

import com.sorokaandriy.user_service.dto.UserEntityRequest;
import com.sorokaandriy.user_service.model.UserEntity;
import com.sorokaandriy.user_service.repository.UserRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    public @Nullable UserEntity createUser(UserEntityRequest request) {
        UserEntity user = mapper.fromUserRequestToUser(request);
        return repository.save(user);
    }
}
