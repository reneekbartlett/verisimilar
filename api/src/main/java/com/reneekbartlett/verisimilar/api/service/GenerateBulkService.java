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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.reneekbartlett.verisimilar.api.util.FileUtils;
import com.reneekbartlett.verisimilar.core.generator.AsyncPersonGenerator;
import com.reneekbartlett.verisimilar.core.io.AsyncFileWriter;
import com.reneekbartlett.verisimilar.core.model.PersonRecord;

@Service
public class GenerateBulkService {

    private final long DEFAULT_LINE_COUNT = 10;
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateBulkService.class);

    @Value("${generator.bulk.max-lines:1000}")
    private Long maxLines;

    @Value("${generator.bulk.outfile.dir:/tmp/verisimilar/outfiles/}")
    private String outfileDir;

    private AsyncPersonGenerator asyncPersonGenerator;

    public GenerateBulkService(AsyncPersonGenerator asyncPersonGenerator) {
        this.asyncPersonGenerator = asyncPersonGenerator;
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
        if(count > maxLines) {
            count = maxLines;
            LOGGER.info("Over max!  Limiting to " + maxLines + " lines.");
        }

        int returnCount = (int)count;
        String[] people = new String[returnCount];
        for (int i = 0; i < count; i++) {
            people[i] = asyncPersonGenerator.generate().toString();
            //LOGGER.info(people[i].toString());
        }
        return people;
    }

    // TODO:  Not currently implemented.  Need to add OUTFILE_DIR as config property, other considerations.
    public String[] generateBulkFile(long lineCount, int fileCount) throws Exception {
        if(lineCount > maxLines) {
            lineCount = maxLines;
            LOGGER.info("Over max!  Limiting to " + maxLines + " lines.");
        }

        int linesPerFile;
        if(lineCount >= Integer.MAX_VALUE) {
            fileCount = 2;
            linesPerFile = Integer.MAX_VALUE;
        } else {
            fileCount = 1;
            linesPerFile = (int)lineCount;
        }

        String[] outFiles = new String[fileCount];
        Semaphore semaphore = new Semaphore(10);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyddMM_HHmmssSSS");
        String ts = LocalDateTime.now().format(formatter);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for(int x = 0; x < fileCount; x++) {
                final int fileIndex = x;
                Path filePath = Path.of(outfileDir, "people_"+lineCount+"_"+ts+"_"+fileIndex+".csv");

                // rename file, don't overwrite/append
                if (Files.exists(filePath)) {
                    FileUtils.rotateFile(filePath);
                }

                var people = new PersonRecord[linesPerFile];
                for (int i = 0; i < linesPerFile; i++) {
                    people[i] = asyncPersonGenerator.generate();
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
