package com.sorolaandriy.notification_service.controller;

import com.sorolaandriy.notification_service.service.TransactionApprovedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verify")
public class NotificationController {

    private final TransactionApprovedService service;

    public NotificationController(TransactionApprovedService service) {
        this.service = service;
    }

    @GetMapping("/approve")
    public ResponseEntity<String> approveTransaction(
            @RequestParam String token
    ){
        return ResponseEntity.ok(service.approveTransaction(token));
    }

    @GetMapping("/reject")
    public ResponseEntity<String> rejectTransaction(
            @RequestParam String token
    ){
        return ResponseEntity.ok(service.rejectTransaction(token));
    }
}
