package com.reneekbartlett.verisimilar.api.controller;

import com.reneekbartlett.verisimilar.api.service.PersonService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    @GetMapping("/generate")
    public Object generate(@RequestParam(defaultValue = "us") String region) {
        return service.generate();
    }

    @GetMapping("/generateBulk")
    public Object generateBulk(@RequestParam(defaultValue = "us") String region) throws Exception {
        return service.generateBulk(10);
    }
}
