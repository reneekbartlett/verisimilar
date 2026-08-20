package com.reneekbartlett.verisimilar.api;

import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;

import com.reneekbartlett.verisimilar.api.model.GeneratorFilter;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;

public class TestWebRequestFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestWebRequestFactory.class);

    private TestWebRequestFactory() {}

    public static NativeWebRequest create(Map<String, String[]> parameterMap) {
        NativeWebRequest nativeWebRequest = Mockito.mock(NativeWebRequest.class);
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);

        if(parameterMap != null) {
            when(nativeWebRequest.getParameterMap()).thenReturn(parameterMap);
            when(httpServletRequest.getParameterMap()).thenReturn(parameterMap);
        }

        when(nativeWebRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(httpServletRequest);

        return nativeWebRequest;
    }

    public static MethodParameter getTestMethodParameter(int parameterIndex) throws NoSuchMethodException, SecurityException {
        Method method = GeneratorFilterResolverTests.class
                .getMethod("testControllerMethod", GeneratorFilter.class, String.class);
        return new SynthesizingMethodParameter(method, 0);
    }

    public static MockHttpServletRequest mockMvcRequest(Consumer<Builder> config) {
        Builder builder = new Builder();
        config.accept(builder);
        return builder.buildMockMvcRequest();
    }

    public static void runTests() {
        NativeWebRequest test1 = TestWebRequestFactory.create(Map.of("id", new String[]{"123"}));
        LOGGER.debug("{}", test1);

        NativeWebRequest test2 = TestWebRequestFactory.builder()
                .param("id", "42")
                .param("tags", "a", "b")
                .header("X-Request-ID", "abc123")
                .body("{\"name\":\"Renee\"}")
                .build();
        LOGGER.debug("{}", test2);

        MockHttpServletRequest test3 = TestWebRequestFactory.mockMvcRequest(b -> 
            b.param("id", "42").header("X-Request-ID", "abc123").body("{\"name\":\"Renee\"}")
        );
        LOGGER.debug("{}", test3);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<String, String[]> params = new HashMap<>();
        private final Map<String, String> headers = new HashMap<>();
        private String body;

        public Builder param(String name, String... values) {
            params.put(name, values);
            return this;
        }

        public Builder header(String name, String value) {
            headers.put(name, value);
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        /***
         * TODO
         * @return
         */
        public MockHttpServletRequest buildMockMvcRequest() {
            MockHttpServletRequest mock = new MockHttpServletRequest();

            params.forEach(mock::addParameter);
            headers.forEach(mock::addHeader);

            if (body != null) {
                mock.setContent(body.getBytes(StandardCharsets.UTF_8));
            }

            return mock;
        }

        public NativeWebRequest build() {
            NativeWebRequest nativeWebRequest = Mockito.mock(NativeWebRequest.class);
            HttpServletRequest servletRequest = Mockito.mock(HttpServletRequest.class);

            when(nativeWebRequest.getParameterMap()).thenReturn(params);
            when(servletRequest.getParameterMap()).thenReturn(params);

            headers.forEach((name, value) ->
                when(servletRequest.getHeader(name)).thenReturn(value)
            );

            if (body != null) {
                try {
                    ServletInputStream inputStream = new DelegatingServletInputStream(
                        new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8))
                    );
                    when(servletRequest.getInputStream()).thenReturn(inputStream);
                } catch (IOException ignored) {}
            }

            when(nativeWebRequest.getNativeRequest(HttpServletRequest.class))
                    .thenReturn(servletRequest);

            return nativeWebRequest;
        }
    }

}
