package com.reneekbartlett.verisimilar.core.datasets.loader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceLoaderUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceLoaderUtil.class);

    private static final String DEFAULT_COL_DELIMITER = ",";

    public boolean exists(String path) {
        return getClass().getClassLoader().getResource(path) != null;
    }

    public ResourceLoaderUtil() {
        //
    }

    public Map<String, Double> loadWeightedMap(String resourcePath) {
        // TODO:  use exists()
        Map<String, Double> map = new HashMap<>();
        try {
            for (String[] parts : readLines(resourcePath)) {
                map.put(parts[0], Double.parseDouble(parts[1]));
            }
        } catch(Exception e) {
            LOGGER.error("Error loading " + resourcePath +  " file", e);
        }
        //LOGGER.debug("Loaded: " + resourcePath);
        return Collections.unmodifiableMap(map);
    }

    public Map<String, String> loadStringMap(String resourcePath, int keyIndex) {
        Map<String, String> map = new HashMap<>();
        try {
            for (String[] parts : readLines(resourcePath)) {
                //TODO
                map.put(parts[1], parts[0]);
            }
        } catch(Exception e) {
            LOGGER.error("Error loading " + resourcePath +  " file", e);
        }
        return Collections.unmodifiableMap(map);
    }

    public Map<String, List<String>> loadListMap(String resourcePath) {
        Map<String, List<String>> map = new HashMap<>();
        try {
            for (String[] parts : readLines(resourcePath)) {
                List<String> list = Arrays.asList(parts[1].split("\\|"));
                map.put(parts[0], Collections.unmodifiableList(list));
            }
        } catch(Exception e) {
            LOGGER.error("Error loading " + resourcePath +  " file", e);
        }
        return Collections.unmodifiableMap(map);
    }

    public Map<String, String[]> loadArrayMap(String resourcePath) {
        Map<String, String[]> map = new HashMap<>();
        try {
            for (String[] parts : readLines(resourcePath)) {
                map.put(parts[0], parts[1].split("\\|"));
            }
        } catch(Exception e) {
            LOGGER.error("Error loading " + resourcePath +  " file", e);
        }
        return Collections.unmodifiableMap(map);
    }

    public String[] loadStringArray(String resourcePath) {
        List<String> vals = new ArrayList<>();
        try {
            for (String[] parts : readLines(resourcePath)) {
                vals.add(parts[0]);
            }
        } catch(Exception e) {
            LOGGER.error("Error loading " + resourcePath +  " file", e);
            return new String[0];
        }
        return vals.toArray(new String[0]);
    }

    public Set<String> loadStringSet(String resourcePath) {
        Set<String> vals = new HashSet<>();
        try {
            for (String[] parts : readLines(resourcePath)) {
                vals.add(parts[0]);
            }
        } catch(Exception e) {
            LOGGER.error("Error loading " + resourcePath +  " file", e);
        }
        return vals;
    }

    private List<String[]> readLines(String resourcePath) throws RuntimeException {
        List<String[]> rows = new ArrayList<>();

        try {
            //InputStream is = ResourceMapLoader.class.getResourceAsStream(resourcePath);
            InputStream is = getResourceAsStream(resourcePath);
            if (is == null) {
                throw new IllegalArgumentException("Dataset not found: " + resourcePath);
            }
    
            //Map<String, Double> map = new HashMap<>();

            try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.isBlank() || line.startsWith("#")) {
                        continue;
                    }
                    rows.add(line.split(DEFAULT_COL_DELIMITER, 2));

                    //String[] parts = line.split(",");
                    //String value = parts[0].trim();
                    //Double weight = Double.parseDouble(parts[1].trim());
                    //map.put(value, weight);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to load resource: " + resourcePath, e);
            }
            return rows;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load dataset: " + resourcePath, e);
        }
    }

    /**
     * Loads a resource as an InputStream from the classpath.
     */
    private InputStream getResourceAsStream(String path) {
        return Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path);
    }
}
