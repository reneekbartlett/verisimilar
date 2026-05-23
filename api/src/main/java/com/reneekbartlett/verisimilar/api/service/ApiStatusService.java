package com.reneekbartlett.verisimilar.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ApiStatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiStatusService.class);

    @Autowired
    private Environment env;

    public void logProperties() {
        String port = env.getProperty("server.port");
        LOGGER.debug("Running on port: " + port);

        String driverClassName = env.getProperty("spring.datasource.driverClassName");
        LOGGER.debug("driverClassName: " + driverClassName);

        String allowUrlApiKeys = env.getProperty("application.security.allow-url-api-keys", "false");
        LOGGER.debug("allowUrlApiKeys: " + allowUrlApiKeys);

        var apiUsers = env.getProperty("application.security.api-users", "");
        LOGGER.debug("apiUsers: " + apiUsers);

        LOGGER.debug("done");
    }
}
