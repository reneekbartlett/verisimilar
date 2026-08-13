package com.reneekbartlett.verisimilar.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

//Example Spring Boot 4 RestTestClient syntax
@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTests {

    @Autowired
    private RestTestClient client; // Hits the actual running port

    //@Test
    void testLiveServer() {
        client.get().uri("/api/generate/fullName").header("X-API-KEY", "SwaggerKey123")
            .exchange()
            .expectStatus().isOk();

    }
}
