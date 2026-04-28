//package com.reneekbartlett.verisimilar.api.config;
//
//import com.reneekbartlett.verisimilar.core.templates.TemplateRegistry;
//import com.reneekbartlett.verisimilar.core.templates.loader.TemplateRegistryLoader;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class TemplateRegistryConfig {
//
//    @Bean(name="usernameTemplateRegistry")
//    public TemplateRegistry usernameTemplateRegistry() {
//        TemplateRegistryLoader loader = new TemplateRegistryLoader();
//        return loader.loadFromClasspath("templates/username-templates.yaml");
//    }
//}
