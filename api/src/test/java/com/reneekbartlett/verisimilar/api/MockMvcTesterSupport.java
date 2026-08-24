package com.reneekbartlett.verisimilar.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;

import org.springframework.core.MethodParameter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResultAssert;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.web.context.request.NativeWebRequest;

import com.reneekbartlett.verisimilar.api.TestControllerMethodFactory.SyntheticController;

import jakarta.servlet.http.HttpServletRequest;

public final class MockMvcTesterSupport {

    private MockMvcTesterSupport() {}

    public static void runTests() {
        Class<?> controllerClass = SyntheticController.class;
        Object controller = instantiate(controllerClass);
        NativeWebRequest req = TestWebRequestFactory.builder()
                .param("id", "42")
                .header("X-Request-ID", "abc123")
                .build();

        MvcTestResultAssert result = MockMvcTesterSupport.performGetWithWebRequest(
                controller,
                "/api/test",
                req
        );
    }

    public static MockMvcTester create(MockMvc mockMvc) {
        return MockMvcTester.create(mockMvc);
    }

    public static MockMvc createMockMvc(Object controller) {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        return mockMvc;
    }

    /**
     * Create a MockMvcTester from a standalone controller instance.
     * This is the modern equivalent of bindToController().
     */
    public static MockMvcTester forController(Object controller) {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        return MockMvcTester.create(mockMvc);
    }

    public static MockMvcTester forControllerClass(Class<?> controllerClass) {
        Object controller = instantiate(controllerClass);
        return forController(controller);
    }

    public static MvcTestResultAssert performGetWithWebRequest(
            //Class<?> controllerClass,
            Object controller,
            String url,
            NativeWebRequest webRequest
    ) {
        //Object controller = instantiate(controllerClass);
        //MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        //MockMvcTester tester1 = MockMvcTester.create(mockMvc);

        //WebApplicationContext webApplicationContext = null;
        //Function<DefaultMockMvcBuilder, MockMvc> customizations;
        //MockMvcTester tester2 = MockMvcTester.from(webApplicationContext, customizations);

        StandaloneMockMvcBuilder builder = MockMvcBuilders.standaloneSetup(controller);
        MockMvc mockMvc = builder.build();
        MockMvcTester tester = MockMvcTester.create(mockMvc);

        // Start building the GET request
        MockMvcTester.MockMvcRequestBuilder requestBuilder = tester.get().uri(url);
        webRequest.getParameterMap().forEach(requestBuilder::param);

        // Copy headers from NativeWebRequest
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        //Enumeration<String> headerNames = webRequest.getNativeRequest(HttpServletRequest.class).getHeaderNames();
        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            requestBuilder.header(headerName, servletRequest.getHeader(headerName));
        }

        //The method tester() is undefined for the type MockMvcTester.MockMvcRequestBuilder
        //return op.execute().tester();
        //return tester;

        return requestBuilder.assertThat();
    }

    public static MockMvcTester forResolverTest(
            Class<?> controllerClass,
            String methodName,
            int parameterIndex,
            Class<?>... parameterTypes
    ) throws NoSuchMethodException {
        Object controller = instantiate(controllerClass);

        MethodParameter methodParameter = TestMethodParameterFactory.forMethod(
                controllerClass,
                methodName,
                parameterIndex,
                parameterTypes
        );
        Method method = methodParameter.getMethod();

        //Method controllerMethod = TestControllerMethodFactory.getMethod(controllerClass, methodName,parameterTypes);

        MockMvcTester tester = forController(controller);

        //Removed in Spring Boot 4
        //tester.get().invoke(method).tester();

        return tester;

        //return MockMvcTester.bindToController(controller).build().request().to(method)tester();
    }

    public static MockMvcTester forControllerMethodWithWebRequest(
            Class<?> controllerClass,
            String methodName,
            NativeWebRequest webRequest,
            Class<?>... parameterTypes
    ) {
        Object controller = instantiate(controllerClass);

        Method method = TestControllerMethodFactory.getMethod(
                controllerClass,
                methodName,
                parameterTypes
        );

        MockMvcTester tester = forController(controller);

        //return tester.request()
        //        .withNativeRequest(webRequest.getNativeRequest(HttpServletRequest.class))
        //        .to(method)
        //        .tester();
        return tester;
    }

    private static <T> T instantiate(Class<T> type) {
        try {
            Constructor<T> ctor = type.getDeclaredConstructor();
            if (!Modifier.isPublic(ctor.getModifiers())) {
                ctor.setAccessible(true);
            }
            return ctor.newInstance();
        } catch (NoSuchMethodException ex) {
            throw new IllegalStateException(
                "No default constructor found for " + type.getName(),
                ex
            );
        } catch (InvocationTargetException ex) {
            throw new IllegalStateException(
                "Constructor of " + type.getName() + " threw an exception.",
                ex.getCause()
            );
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException(
                "Unable to instantiate controller " + type.getName(),
                ex
            );
        }
    }
}

