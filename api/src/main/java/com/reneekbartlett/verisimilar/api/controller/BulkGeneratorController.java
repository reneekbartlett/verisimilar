package com.reneekbartlett.verisimilar.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reneekbartlett.verisimilar.api.service.GenerateBulkService;
import com.reneekbartlett.verisimilar.api.service.FileOutputService;
import com.reneekbartlett.verisimilar.api.service.FileOutputService.ExportData;

//import io.micrometer.core.instrument.Timer;
//import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/generateBulk")
public class BulkGeneratorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BulkGeneratorController.class);

    private final GenerateBulkService bulkService;
    private final FileOutputService fileService;

    @Autowired
    public BulkGeneratorController(
            GenerateBulkService bulkService, 
            FileOutputService fileService) {
        this.bulkService = bulkService;
        this.fileService = fileService;
    }

    @GetMapping
    public ResponseEntity<String[]> generate(
            @RequestParam(name = "lineCount", required = false) Long lineCount
    ) {
        try {
            String[] out = bulkService.generateBulk(lineCount);
            return ResponseEntity.ok(out);
        } catch (Exception e) {
            LOGGER.error("Error.", e);
            return ResponseEntity.internalServerError().body(new String[0]);
        }
    }

    public ResponseEntity<Resource> downloadFile() {
        ExportData data = new ExportData("Sample Report", "This is generated text content.");
        Resource resource = fileService.generateTextFile(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.txt\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }
}
