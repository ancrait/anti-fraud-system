package com.sorokaandriy.user_service.controller;

import com.sorokaandriy.user_service.UserServiceApplication;
import com.sorokaandriy.user_service.dto.UpdateUserEntityRequest;
import com.sorokaandriy.user_service.dto.UserEntityRequest;
import com.sorokaandriy.user_service.model.UserEntity;
import com.sorokaandriy.user_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserEntity> createUser(
            @RequestBody UserEntityRequest request
    ){
        return ResponseEntity.ok(service.createUser(request));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserEntity> updateUser(
            @RequestBody UpdateUserEntityRequest request,
            @PathVariable String userId){
        return ResponseEntity.ok(service.updateUser(request,userId));
    }

    @PutMapping("/{userId}/{userStatus}")
    public ResponseEntity<UserEntity> updateStatus(
            @PathVariable String userId,
            @PathVariable String userStatus
    ){
        return ResponseEntity.ok(service.updateStatus(userId,userStatus));
    }
}
