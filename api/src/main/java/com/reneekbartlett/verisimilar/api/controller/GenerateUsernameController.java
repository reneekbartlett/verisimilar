package com.reneekbartlett.verisimilar.api.controller;

import com.reneekbartlett.verisimilar.api.service.GenerateUsernameService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generate/username")
public class GenerateUsernameController {

    private final GenerateUsernameService generateService;

    public GenerateUsernameController(GenerateUsernameService generateService) {
        this.generateService = generateService;
    }

    @GetMapping
    public ResponseEntity<Object> generate(
            @RequestParam String first,
            @RequestParam String last,
            @RequestParam(defaultValue = "us") String region
    ) {
        String username = generateService.generateUsername(first, last, region);
        return ResponseEntity.ok().body(username);
    }
}
