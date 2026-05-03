package com.reneekbartlett.verisimilar.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TestGeneratePersonController {

    @GetMapping("/api-test/generate/person")
    public String generatePerson() {
        return "ok";
    }
}
