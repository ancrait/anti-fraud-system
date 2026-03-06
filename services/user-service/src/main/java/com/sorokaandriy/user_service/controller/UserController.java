package com.sorokaandriy.user_service.controller;

import com.sorokaandriy.user_service.UserServiceApplication;
import com.sorokaandriy.user_service.dto.UpdateUserEntityRequest;
import com.sorokaandriy.user_service.dto.UserEntityRequest;
import com.sorokaandriy.user_service.model.UserEntity;
import com.sorokaandriy.user_service.model.UserStatus;
import com.sorokaandriy.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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


    @GetMapping
    public ResponseEntity<Page<UserEntity>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return ResponseEntity.ok(service.findAll(page,size,sortBy));
    }

    @PostMapping
    public ResponseEntity<UserEntity> createUser(
            @Valid @RequestBody UserEntityRequest request
    ){
        return ResponseEntity.ok(service.createUser(request));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserEntity> updateUser(
            @Valid @RequestBody UpdateUserEntityRequest request,
            @PathVariable String userId){
        return ResponseEntity.ok(service.updateUser(request,userId));
    }

    @PutMapping("/{userId}/{userStatus}")
    public ResponseEntity<UserEntity> updateStatus(
            @PathVariable String userId,
            @PathVariable UserStatus userStatus
    ){
        return ResponseEntity.ok(service.updateStatus(userId,userStatus));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(
            @PathVariable String userId
    ){
        return ResponseEntity.ok(service.deleteUser(userId));
    }
}
