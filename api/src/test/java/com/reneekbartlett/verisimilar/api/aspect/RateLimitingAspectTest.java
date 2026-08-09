package com.reneekbartlett.verisimilar.api.aspect;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

//import org.assertj.core.api.InstanceOfAssertFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bucket4j.TimeMeter;

import com.reneekbartlett.verisimilar.api.exception.GlobalExceptionHandler;
import com.reneekbartlett.verisimilar.api.shared.annotation.RateLimited;

@SpringBootTest(classes = {
    RateLimitingAspectTest.TestController.class, 
    RateLimitingAspect.class
})
@Import(GlobalExceptionHandler.class)
public class RateLimitingAspectTest {

    //private static final Logger LOGGER = LoggerFactory.getLogger(RateLimitingAspectTest.class);
    private MockMvcTester mvc;
    private TestController testController;
    private RateLimitingAspect rateLimitingAspect;

    @BeforeEach
    public void setup() {
        // Initialize components, then create AspectJ proxy to add RateLimitingAspect to TestController 
        this.testController = new TestController();
        this.rateLimitingAspect = new RateLimitingAspect();

        AspectJProxyFactory factory = new AspectJProxyFactory(testController);
        factory.addAspect(rateLimitingAspect);
        TestController proxiedController = factory.getProxy();

        // Build MockMvcTester for TestController proxy with GlobalExceptionHandler advice
        this.mvc = MockMvcTester.of(
            List.of(proxiedController), 
            builder -> builder.setControllerAdvice(new GlobalExceptionHandler()).build()
        );

        // For MockMvc (instead of MockMvcTester), initialize manually to combine controller and global exception handler
        //this.mockMvc = MockMvcBuilders.standaloneSetup(testController).setControllerAdvice(globalExceptionHandler).build();
    }

    @Test
    public void testRateLimiter_AllowsRequestsWithinLimit_AndBlocksWhenExceeded() throws Exception {
        String testIp = "192.168.1.50";

        // First request should succeed (Remaining: 2)
        mvc.perform(get("/test/limited").header("X-Forwarded-For", testIp))
            .assertThat()
                .hasStatusOk().hasHeader("X-RateLimit-Limit", "3").hasHeader("X-RateLimit-Remaining", "2");

        // Second request should succeed (Remaining: 1)
        mvc.perform(get("/test/limited").header("X-Forwarded-For", testIp))
            .assertThat()
                .hasStatusOk().hasHeader("X-RateLimit-Limit", "3").hasHeader("X-RateLimit-Remaining", "1");

        // Third request should succeed (Remaining: 0)
        mvc.perform(get("/test/limited").header("X-Forwarded-For", testIp))
            .assertThat()
                .hasStatusOk().hasHeader("X-RateLimit-Limit", "3").hasHeader("X-RateLimit-Remaining", "0");

        // Fourth request exceeds the limit of 3 (Expect HTTP 429)
        mvc.perform(get("/test/limited").header("X-Forwarded-For", testIp))
            .assertThat()
                .hasStatus(HttpStatus.TOO_MANY_REQUESTS)
                .hasHeader("X-RateLimit-Limit", "3")
                .hasHeader("X-RateLimit-Remaining", "0")
                .containsHeader("X-RateLimit-Reset")
                .containsHeader("Retry-After")
                .bodyJson().extractingPath("error").asString().isEqualTo("Too Many Requests");
    }

    @Test
    public void testRateLimiter_IsolatesLimitsByIpAddress() throws Exception {
        String ipUserA = "10.0.0.1";
        String ipUserB = "10.0.0.2";

        // Exhaust UserA bucket (3 requests)
        for (int i = 0; i < 3; i++) {
            mvc.perform(get("/test/limited").header("X-Forwarded-For", ipUserA))
                .assertThat().hasStatusOk();
        }

        // UserA's 4th request gets blocked
        mvc.perform(get("/test/limited").header("X-Forwarded-For", ipUserA))
            .assertThat().hasStatus(HttpStatus.TOO_MANY_REQUESTS);

        // UserB for exact same endpoint, but should be allowed bc they have separate bucket
        mvc.perform(get("/test/limited").header("X-Forwarded-For", ipUserB))
            .assertThat().hasStatusOk().hasHeader("X-RateLimit-Remaining", "2");
    }

    @Test
    public void testRateLimiter_RefillsTokens_AfterTimeWindowPasses() throws Exception {
        String testIp = "192.168.5.500";

        // Custom clock starting at 0ms
        class ControllableClock implements TimeMeter {
            long currentTimeMillis = 0;
            @Override public long currentTimeNanos() { return currentTimeMillis * 1_000_000; }
            @Override public boolean isWallClockBased() { return false; }
        }

        ControllableClock customClock = new ControllableClock();

        // Inject ControllableClock into the active aspect bean
        rateLimitingAspect.setTimeMeter(customClock);

        // Exhaust bucket(3 requests)
        mvc.perform(get("/test/limited").header("X-Forwarded-For", testIp)).assertThat().hasStatusOk();
        mvc.perform(get("/test/limited").header("X-Forwarded-For", testIp)).assertThat().hasStatusOk();
        mvc.perform(get("/test/limited").header("X-Forwarded-For", testIp)).assertThat().hasStatusOk();

        // Verify 4th request is blocked (Bucket is empty)
        mvc.perform(get("/test/limited").header("X-Forwarded-For", testIp))
            .assertThat().hasStatus(HttpStatus.TOO_MANY_REQUESTS);

        // Fast-forward customClock by 11s to jump past 10s durationSeconds threshold
        customClock.currentTimeMillis += 11_000;
        rateLimitingAspect.setTimeMeter(customClock);

        // Verify the bucket refilled (Request should succeed again)
        mvc.perform(get("/test/limited").header("X-Forwarded-For", testIp))
            .assertThat().hasStatusOk().hasHeader("X-RateLimit-Remaining", "2");
    }

    // Local controller to test Aspect boundary rules
    @RestController
    public static class TestController {
        // Set Low limit of 3 requests per 10 seconds just for testing
        @RateLimited(capacity = 3, durationSeconds = 10)
        @GetMapping("/test/limited")
        public String limitedEndpoint() {
            return "success";
        }
    }

}
