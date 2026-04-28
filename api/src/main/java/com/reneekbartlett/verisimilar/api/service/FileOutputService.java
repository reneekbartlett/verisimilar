package com.reneekbartlett.verisimilar.api.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

@Service
public class FileOutputService {
    
    
    
    public record ExportData(String title, String content) {}

    public InputStreamResource generateTextFile(ExportData data) {
        String fullText = "Title: " + data.title() + "\n\n" + data.content();
        return new InputStreamResource(
                //new ByteArrayInputStream(fullText.getBytes())
                new ByteArrayResource(fullText.getBytes())
        );
    }
}
