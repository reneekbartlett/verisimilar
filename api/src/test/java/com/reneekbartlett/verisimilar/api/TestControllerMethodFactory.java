package com.reneekbartlett.verisimilar.api;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.web.bind.annotation.RequestParam;

public final class TestControllerMethodFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestControllerMethodFactory.class);

    private TestControllerMethodFactory() {}

    public static class SyntheticController {
        public void noArgs() {}
        public void oneArg(String value) {}
        public void twoArgs(String a, Integer b) {}
        public void annotatedArg(@RequestParam("id") String id) {}
    }

    public static Method get(String methodName, Class<?>... parameterTypes) {
        try {
            return SyntheticController.class.getMethod(methodName, parameterTypes);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static MethodParameter param(String methodName, int index, Class<?>... parameterTypes) {
        return new SynthesizingMethodParameter(get(methodName, parameterTypes), index);
    }

    public static Method getMethod(
            Class<?> controllerClass,
            String methodName,
            Class<?>... parameterTypes
    ) {
        try {
            return controllerClass.getMethod(methodName, parameterTypes);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                "Unable to resolve method " + controllerClass.getName() +
                "#" + methodName, ex
            );
        }
    }

    public static MethodParameter getMethodParameter(
            Class<?> controllerClass,
            String methodName,
            int parameterIndex,
            Class<?>... parameterTypes
    ) {
        Method method = getMethod(controllerClass, methodName, parameterTypes);
        return new SynthesizingMethodParameter(method, parameterIndex);
    }

    public static void runTests() {
        MethodParameter mp = TestControllerMethodFactory.param("annotatedArg", 0, String.class);
        LOGGER.debug("{}", mp.toString());
    }
}
