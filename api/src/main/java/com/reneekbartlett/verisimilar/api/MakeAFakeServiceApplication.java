package com.reneekbartlett.verisimilar.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class MakeAFakeServiceApplication extends SpringBootServletInitializer {

    //private Logger logger = LoggerFactory.getLogger(WebApplication.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MakeAFakeServiceApplication.class, args);
    }
}
