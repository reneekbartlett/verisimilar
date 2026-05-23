package com.reneekbartlett.verisimilar.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reneekbartlett.verisimilar.api.service.ApiStatusService;

@RestController
@RequestMapping("/api/public/status")
public class ApiStatusController {

    private final ApiStatusService apiStatusService;

    @Autowired
    public ApiStatusController(ApiStatusService apiStatusService) {
        this.apiStatusService = apiStatusService;
    }

    @GetMapping
    public ResponseEntity<Object> status() {

        apiStatusService.logProperties();

        return ResponseEntity.ok().body("OK");
    }
}
