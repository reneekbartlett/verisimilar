package com.reneekbartlett.verisimilar.core.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ResourceMapLoader {

    private static Logger logger = LoggerFactory.getLogger(ResourceMapLoader.class);

    private static final String DEFAULT_COL_DELIMITER = ",";

    private ResourceMapLoader() {}

    public static Map<String, Double> loadDoubleMap(String resourcePath) {
        Map<String, Double> map = new HashMap<>();
        try {
            for (String[] parts : readLines(resourcePath)) {
                map.put(parts[0], Double.parseDouble(parts[1]));
            }
        } catch(Exception e) {
            logger.error("Error loading " + resourcePath +  " file", e);
        }
        return Collections.unmodifiableMap(map);
    }

    public static Map<String, String> loadStringMap(String resourcePath) {
        Map<String, String> map = new HashMap<>();
        try {
            for (String[] parts : readLines(resourcePath)) {
                map.put(parts[0], parts[1]);
            }
        } catch(Exception e) {
            logger.error("Error loading " + resourcePath +  " file", e);
        }
        return Collections.unmodifiableMap(map);
    }

    public static Map<String, List<String>> loadListMap(String resourcePath) {
        Map<String, List<String>> map = new HashMap<>();
        try {
            for (String[] parts : readLines(resourcePath)) {
                List<String> list = Arrays.asList(parts[1].split("\\|"));
                map.put(parts[0], Collections.unmodifiableList(list));
            }
        } catch(Exception e) {
            logger.error("Error loading " + resourcePath +  " file", e);
        }
        return Collections.unmodifiableMap(map);
    }

    public static Map<String, String[]> loadArrayMap(String resourcePath) {
        Map<String, String[]> map = new HashMap<>();
        try {
            for (String[] parts : readLines(resourcePath)) {
                map.put(parts[0], parts[1].split("\\|"));
            }
        } catch(Exception e) {
            logger.error("Error loading " + resourcePath +  " file", e);
        }
        return Collections.unmodifiableMap(map);
    }

    private static List<String[]> readLines(String resourcePath) {
        List<String[]> rows = new ArrayList<>();
        try (
            InputStream is = ResourceMapLoader.class.getResourceAsStream(resourcePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }
                rows.add(line.split(DEFAULT_COL_DELIMITER, 2));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load resource: " + resourcePath, e);
        }
        return rows;
    }
}
