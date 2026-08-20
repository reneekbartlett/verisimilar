package com.reneekbartlett.verisimilar.core;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PropertyLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyLoader.class);

    private static final Map<String, String> REGISTRY = new ConcurrentHashMap<>();

    private static final CompositeConfiguration CONFIG = new CompositeConfiguration();

    private static final String DEFAULT_CONFIG_FILE = "config.properties";
    private static final String CONFIG_PATH_KEY = "app.config.path";

    static {
        try {
            Configurations configurationsFactory = new Configurations();

            // Add Command line -D arguments to the top of stack (1st priority)
            CONFIG.addConfiguration(new SystemConfiguration());

            Configuration fileConfig;

            // If app.config.path is included in command line, use it.
            String externalPath = System.getProperty(CONFIG_PATH_KEY);
            if (externalPath != null && !externalPath.isBlank()) {
                fileConfig = configurationsFactory.properties(new File(externalPath));
            } else {
                URL resource = PropertyLoader.class.getClassLoader().getResource(DEFAULT_CONFIG_FILE);
                if (resource == null) {
                    throw new IllegalStateException("Critical Error: Default '" + DEFAULT_CONFIG_FILE + "' not found on classpath!");
                }
                fileConfig = configurationsFactory.properties(resource);
                LOGGER.info("Loaded default configurations from classpath: {}", DEFAULT_CONFIG_FILE);
            }

            // Add file properties beneath System Properties
            CONFIG.addConfiguration(fileConfig);

        } catch (ConfigurationException e) {
            throw new ExceptionInInitializerError("Initialization failed: " + e.getMessage());
        }
    }

    private PropertyLoader() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Bridges the API module's configuration data into the Core library memory.
     */
    public static void initializeCoreProperties(Map<String, String> properties) {
        REGISTRY.clear();
        REGISTRY.putAll(properties);
        System.out.println("--> Core PropertyLoader successfully bridged with API environment.");
    }

    /**
     * Triggers the static initialization block to validate configs on boot.
     * Throws an ExceptionInInitializerError if files are corrupt or missing.
     */
    public static void validateOnBoot() {
        // Read an arbitrary default or framework-level key just to force a system check
        // Apache Commons Configuration will automatically evaluate the ecosystem here
        getString("app.environment", "production");
        LOGGER.info("--> Boot check: Application configurations validated successfully.");
    }

    // 
    // Type-Safe Public API Wrappers
    //

    public static String getString(String key) { return CONFIG.getString(key); }

    public static String getString(String key, String defaultValue) {
        return CONFIG.getString(key, defaultValue);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return CONFIG.getBoolean(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return CONFIG.getInt(key, defaultValue);
    }

    public static String[] getStringArray(String key) {
        return CONFIG.getStringArray(key);
    }
}
