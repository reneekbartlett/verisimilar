package com.reneekbartlett.verisimilar.api.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.reneekbartlett.verisimilar.api.dto.PersonResponseDto;
import com.reneekbartlett.verisimilar.core.generator.PersonGenerator;
import com.reneekbartlett.verisimilar.core.io.AsyncFileWriter;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.PersonRecord;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

@Service
public class PersonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    //private static final String FIELD_DELIM = "\t";
    private static final Long MAX_LINES = 10000L;

    // TODO:  use config
    private static final String OUTFILE_DIR = "/opt/reneekbartlett/outfiles/";

    private final PersonGenerator personGenerator;

    public PersonService(PersonGenerator personGenerator) {
        this.personGenerator = personGenerator;
    }

    public PersonResponseDto generate() {
        DatasetResolutionContext ctx = DatasetResolutionContext.builder().build();
        SelectionFilter filter = SelectionFilter.builder().build();

        PersonRecord person = personGenerator.generate(ctx, filter);

        return new PersonResponseDto(person.firstName(), person.middleName(), person.lastName(), 
                person.birthday(),
                // person.gender(),
                GenderIdentity.GENDER_UNSPECIFIED, person.address1(), // String address1,
                person.address2(), // String address2,
                person.city(), // String city,
                person.state(), // String state,
                person.zip(), // String zip,
                person.emailAddress().email(), // String emailAddress,
                person.phoneNumber().toString() // String phoneNumber
        );
    }

    public String[] generateBulk(long count) throws Exception {
        if (count > MAX_LINES) {
            count = MAX_LINES;
            LOGGER.info("Over max!  Limiting to " + MAX_LINES + " lines.");
        }

        int fileCount;
        int linesPerFile;
        if (count >= Integer.MAX_VALUE) {
            fileCount = 2;
            linesPerFile = Integer.MAX_VALUE;
        } else {
            fileCount = 1;
            linesPerFile = (int) count;
        }

        String[] outFiles = new String[fileCount];
        Semaphore semaphore = new Semaphore(10);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyddMM_HHmmssSSS");
        String ts = LocalDateTime.now().format(formatter);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int x = 0; x < fileCount; x++) {
                final int fileIndex = x;
                Path filePath = Path.of(OUTFILE_DIR, "people_" + count + "_" + ts + "_" + fileIndex + ".csv");

                // rename file, don't overwrite/append
                if (Files.exists(filePath)) {
                    rotateFile(filePath);
                }

                // var people = new Person[linesPerFile];
                //var people = new FakePersonEntry[linesPerFile];
                var people = new PersonRecord[linesPerFile];
                for (int i = 0; i < linesPerFile; i++) {
                    people[i] = personGenerator.generate();
                    LOGGER.info(people[i].toString());
                }

                semaphore.acquire();

                // TODO: HEADER?
                executor.submit(() -> {
                    try {
                        // AsyncFileWriter.writeObjectsAsync(people, filePath).join();
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

    // TODO:  FileUtils
    private static Optional<String> getFileExtensionFromName(String fileName) {
        return Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1));
    }

    // TODO:  FileUtils
    private static void rotateFile(Path filePath) throws IOException {
        String fileName = filePath.getFileName().toString();
        String ext = getFileExtensionFromName(fileName).orElse("");
        String rotatedName = fileName + "_" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())
                + (ext.isEmpty() ? "" : "." + ext);
        Files.move(filePath, filePath.resolveSibling(rotatedName), StandardCopyOption.REPLACE_EXISTING);
    }
}
