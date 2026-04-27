package com.reneekbartlett.verisimilar.api.service;

import org.springframework.stereotype.Service;

//import com.reneekbartlett.verisimilar.core.generator.PersonGenerator;

@Service
public class GeneratorService {

    private final long DEFAULT_LINE_COUNT = 10;

    public String[] generateBulk() throws Exception{
        //return PersonGenerator.generateBulk(DEFAULT_LINE_COUNT);
        return generateBulk(DEFAULT_LINE_COUNT);
    }

    public String[] generateBulk(Long lineCount) throws Exception{
        if(lineCount == null) {
            lineCount = DEFAULT_LINE_COUNT;
        }
        
        //PersonRecordGenerator
        
        // TODO
        return new String[0];
        //return PersonGenerator.generateBulk(lineCount);
    }
}
