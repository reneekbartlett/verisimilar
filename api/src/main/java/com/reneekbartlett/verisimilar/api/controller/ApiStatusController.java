package com.reneekbartlett.verisimilar.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/status")
public class ApiStatusController {

    public ApiStatusController() {
        //
    }

    @GetMapping
    public ResponseEntity<Object> status() {
        return ResponseEntity.ok().body("OK");
    }
}
