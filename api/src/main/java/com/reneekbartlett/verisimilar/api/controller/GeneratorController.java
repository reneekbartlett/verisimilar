//package com.reneekbartlett.verisimilar.api.controller;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.reneekbartlett.verisimilar.api.service.GeneratorService;
//
//import io.micrometer.core.instrument.Timer;
//import lombok.extern.slf4j.Slf4j;
//
//@RestController
//public class GeneratorController {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorController.class);
//    private final GeneratorService generatorService;
//
//    @Autowired
//    public GeneratorController(GeneratorService generatorService) {
//        this.generatorService = generatorService;
//    }
//
//    @GetMapping("/generate")
//    public ResponseEntity<String[]> generate(
//            @RequestParam(name = "lineCount", required = false) Long lineCount
//    ) {
//        try {
//            String[] out = generatorService.generateBulk(lineCount);
//            return ResponseEntity.ok(out);
//        } catch (Exception e) {
//            LOGGER.error("Error.", e);
//            return ResponseEntity.internalServerError().body(new String[0]);
//        }
//    }
//}
