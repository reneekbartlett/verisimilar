package com.reneekbartlett.verisimilar.api.controller;

import com.reneekbartlett.verisimilar.api.service.UsernameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usernames")
public class UsernameController {

    private final UsernameService service;

    public UsernameController(UsernameService service) {
        this.service = service;
    }

    @GetMapping
    public String generate(
            @RequestParam String first,
            @RequestParam String last,
            @RequestParam(defaultValue = "us") String region
    ) {
        return service.generateUsername(first, last, region);
    }
}
