package com.reneekbartlett.verisimilar.api.config;

import com.reneekbartlett.verisimilar.api.service.GeneratorFilterResolver;
import com.reneekbartlett.verisimilar.api.service.StringToEthnicityConverter;
import com.reneekbartlett.verisimilar.api.service.StringToGenderIdentityConverter;
import com.reneekbartlett.verisimilar.api.service.StringToGenerationConverter;

import java.util.List;

import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StringToGenderIdentityConverter genderIdentityConverter;
    private final StringToGenerationConverter generationConverter;
    private final StringToEthnicityConverter ethnicityConverter;

    public WebConfig(
            StringToGenderIdentityConverter genderIdentityConverter,
            StringToGenerationConverter generationConverter,
            StringToEthnicityConverter ethnicityConverter
    ) {
        this.genderIdentityConverter = genderIdentityConverter;
        this.generationConverter = generationConverter;
        this.ethnicityConverter = ethnicityConverter;
    }

    /***
     * Custom Resolver for GeneratorFilter parameter
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new GeneratorFilterResolver());
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> factory.addConnectorCustomizers(connector -> {
            // Allows unencoded [ and ] ONLY in the GET query parameters
            connector.setProperty("relaxedQueryChars", "[]");
        });
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(genderIdentityConverter);
        registry.addConverter(generationConverter);
        registry.addConverter(ethnicityConverter);
    }

}
