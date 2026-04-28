package com.reneekbartlett.verisimilar.api.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.reneekbartlett.verisimilar.api.util.FileUtils;
import com.reneekbartlett.verisimilar.core.generator.PersonGenerator;
import com.reneekbartlett.verisimilar.core.io.AsyncFileWriter;
import com.reneekbartlett.verisimilar.core.model.PersonRecord;

@Service
public class GenerateBulkService {

    private final long DEFAULT_LINE_COUNT = 10;
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateBulkService.class);

    private static final String FIELD_DELIM = "\t";
    private static final Long MAX_LINES = 10000L;
    private static final String OUTFILE_DIR = "/opt/reneekbartlett/outfiles/";

    private PersonGenerator personGenerator;

    public GenerateBulkService(PersonGenerator personGenerator) {
        this.personGenerator = personGenerator;
    }

    /***
     * Use default line count.
     * @return
     * @throws Exception
     */
    public String[] generateBulk() throws Exception{
        return generateBulk(DEFAULT_LINE_COUNT);
    }

    public String[] generateBulk(long count) throws Exception {
        if(count > MAX_LINES) {
            count = MAX_LINES;
            LOGGER.info("Over max!  Limiting to " + MAX_LINES + " lines.");
        }

        int fileCount;
        int linesPerFile;
        if(count >= Integer.MAX_VALUE) {
            fileCount = 2;
            linesPerFile = Integer.MAX_VALUE;
        } else {
            fileCount = 1;
            linesPerFile = (int)count;
        }

        String[] outFiles = new String[fileCount];
        Semaphore semaphore = new Semaphore(10);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyddMM_HHmmssSSS");
        String ts = LocalDateTime.now().format(formatter);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for(int x = 0; x < fileCount; x++) {
                final int fileIndex = x;
                Path filePath = Path.of(OUTFILE_DIR, "people_"+count+"_"+ts+"_"+fileIndex+".csv");

                // rename file, don't overwrite/append
                if (Files.exists(filePath)) {
                    FileUtils.rotateFile(filePath);
                }

                var people = new PersonRecord[linesPerFile];
                for (int i = 0; i < linesPerFile; i++) {
                    people[i] = personGenerator.generate();
                    //LOGGER.info(people[i].toString());
                }

                semaphore.acquire();

                // TODO:  HEADER?
                executor.submit(() -> {
                    try {
                        //AsyncFileWriter.writeObjectsAsync(people, filePath).join();
                        AsyncFileWriter.writeCsvAsync(people, filePath).join();
                        LOGGER.info("Write complete! " + filePath);
                    } catch (CompletionException | IOException ex) {
                        LOGGER.error("Async write failed for " + filePath, ex);
                        
                    } finally {
                        semaphore.release();
                    }
                    return null;
                });
        
                outFiles[x] = filePath.toString();
            }
        }

        return outFiles;
    }

    
}
