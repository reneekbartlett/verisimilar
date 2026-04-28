package com.reneekbartlett.verisimilar.api.config;

// FYI: Gemini hallucination->import org.springframework.boot.autoconfigure.jackson.JsonMapperBuilderCustomizer;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature; // Import the NEW feature enum

@Configuration
public class JacksonConfig {
    @Bean
    public JsonMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            // Jackson 3 defaults to ISO-8601 strings. 
            // If you WANT timestamps, enable this here:
            builder.disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS);

            //builder.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        };
    }
}
