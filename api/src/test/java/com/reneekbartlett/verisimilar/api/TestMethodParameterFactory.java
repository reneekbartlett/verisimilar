package com.reneekbartlett.verisimilar.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.SynthesizingMethodParameter;

import com.reneekbartlett.verisimilar.api.model.GeneratorFilter;

import java.lang.reflect.Method;

public final class TestMethodParameterFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestMethodParameterFactory.class);

    private TestMethodParameterFactory() {}

    public static MethodParameter forMethod(
            Class<?> declaringClass,
            String methodName,
            int parameterIndex,
            Class<?>... parameterTypes
    ) throws NoSuchMethodException {
        Method method = declaringClass.getMethod(methodName, parameterTypes);
        return new SynthesizingMethodParameter(method, parameterIndex);
    }

    public static MethodParameter forIndex(String methodName, int parameterIndex)
            throws NoSuchMethodException {

        Method method = GeneratorFilterResolverTests.class
                .getMethod("testControllerMethod", GeneratorFilter.class, String.class);

        return new SynthesizingMethodParameter(method, parameterIndex);
    }

    public static void runTests() {
        LOGGER.debug("");
    }

    /***
     * 
     * @param declaringClass
     * @param methodName
     * @param parameterIndex
     * @param setAccessible
     * @param parameterTypes
     * @return
     */
    public static MethodParameter forMethod(
            Class<?> declaringClass,
            String methodName,
            int parameterIndex,
            boolean setAccessible,
            Class<?>... parameterTypes
    ) {
        try {
            Method method = declaringClass.getDeclaredMethod(methodName, parameterTypes);
            if(setAccessible) {
                method.setAccessible(true);
            }
            return new SynthesizingMethodParameter(method, parameterIndex);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                "Unable to resolve MethodParameter for " +
                declaringClass.getName() + "#" + methodName +
                " at index " + parameterIndex, ex
            );
        }
    }

    public static Builder builder(Class<?> declaringClass, String methodName) {
        return new Builder(declaringClass, methodName);
    }

    public static class Builder {
        private final Class<?> declaringClass;
        private final String methodName;
        private Class<?>[] parameterTypes = new Class<?>[0];
        private int parameterIndex = 0;

        Builder(Class<?> declaringClass, String methodName) {
            this.declaringClass = declaringClass;
            this.methodName = methodName;
        }

        public Builder parameterTypes(Class<?>... types) {
            this.parameterTypes = types;
            return this;
        }

        public Builder index(int index) {
            this.parameterIndex = index;
            return this;
        }

        public MethodParameter build() {
            try {
                Method method = declaringClass.getMethod(methodName, parameterTypes);
                return new SynthesizingMethodParameter(method, parameterIndex);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
