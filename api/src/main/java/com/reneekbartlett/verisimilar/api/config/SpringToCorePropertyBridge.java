package com.reneekbartlett.verisimilar.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.reneekbartlett.verisimilar.core.ConfigKeys;
import com.reneekbartlett.verisimilar.core.PropertyLoader;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

// TODO:  Need this?
@Configuration
public class SpringToCorePropertyBridge {

    private final Environment springEnvironment;

    // Inject Spring Boot's active global environment context
    public SpringToCorePropertyBridge(Environment springEnvironment) {
        this.springEnvironment = springEnvironment;
    }

    /**
     * Executes immediately after Spring context initialization completes.
     * Extract values using your Core library's ConfigKeys compilation registry
     * Spring handles the application.properties parsing, JVM flags, and defaults.
     */
    @PostConstruct
    public void bridgePropertiesOnBoot() {
        Map<String, String> coreConfigMap = new HashMap<>();

        //
        // DATASETS
        //
        coreConfigMap.put(ConfigKeys.Datasets.DATASETS_DIR, springEnvironment.getProperty(ConfigKeys.Datasets.DATASETS_DIR, "datasets"));

        //
        // TEMPLATES
        //
        coreConfigMap.put(ConfigKeys.Templates.TEMPLATES_DIR, springEnvironment.getProperty(ConfigKeys.Templates.TEMPLATES_DIR, "templates"));

        // Push the compiled configuration payload down across the library bridge into Core memory
        // TODO:
        //PropertyLoader.initializeCoreProperties(coreConfigMap);
    }
}
