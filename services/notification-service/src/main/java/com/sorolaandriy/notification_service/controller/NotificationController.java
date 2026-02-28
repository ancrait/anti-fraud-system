package com.sorolaandriy.notification_service.controller;

import com.sorolaandriy.notification_service.service.TransactionApprovedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/verify")
public class NotificationController {

    private final TransactionApprovedService service;

    public NotificationController(TransactionApprovedService service) {
        this.service = service;
    }

    @PutMapping("/approve")
    public ResponseEntity<String> approveTransaction(
            @RequestParam String token
    ){
        return ResponseEntity.ok(service.approveTransaction(token));
    }

    @PutMapping("/reject")
    public ResponseEntity<String> rejectTransaction(
            @RequestParam String token
    ){
        return ResponseEntity.ok(service.rejectTransaction(token));
    }
}
