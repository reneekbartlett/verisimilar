package com.reneekbartlett.verisimilar.api.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class FileUtils {
    public static void rotateFile(Path filePath) throws IOException {
        String fileName = filePath.getFileName().toString();
        String ext = getFileExtensionFromName(fileName).orElse("");
        String rotatedName = fileName + "_" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()) 
                + (ext.isEmpty() ? "" : "." + ext);
        Files.move(filePath, filePath.resolveSibling(rotatedName), StandardCopyOption.REPLACE_EXISTING);
    }

    public static Optional<String> getFileExtensionFromName(String fileName) {
        return Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1));
    }
}
