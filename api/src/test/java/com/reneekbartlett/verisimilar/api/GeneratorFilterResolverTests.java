package com.reneekbartlett.verisimilar.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;

import com.reneekbartlett.verisimilar.api.model.FilterCondition;
import com.reneekbartlett.verisimilar.api.model.FilterOperator;
import com.reneekbartlett.verisimilar.api.model.GeneratorFilter;
import com.reneekbartlett.verisimilar.api.service.GeneratorFilterResolver;
import com.reneekbartlett.verisimilar.api.service.SelectionFilterConverter;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import jakarta.servlet.http.HttpServletRequest;

public class GeneratorFilterResolverTests {

    @Test
    public void testGeneratorFilterResolverFirstNameStartsWith() throws NoSuchMethodException, SecurityException {
        GeneratorFilterResolver generatorFilterResolver = new GeneratorFilterResolver();

        // MethodParameter
        MethodParameter methodParameter = getMethodParameterForTest(0);

        // NativeWebRequest: Mock the incoming web request dependencies
        // String query = "filter[FIRST_NAME][startswith]=REN";
        Map<String, String[]> paramMap = new LinkedHashMap<>();
        paramMap.put("filter[FIRST_NAME][startswith]", new String[] { "REN" });
        NativeWebRequest nativeWebRequest = getNativeWebRequestForTest(paramMap);

        // resolveArgument(MethodParameter,ModelAndViewContainer,NativeWebRequest,WebDataBinderFactory)
        Object result = generatorFilterResolver
                .resolveArgument(methodParameter, null, nativeWebRequest, null);

        //
        // GeneratorFilter
        //
        GeneratorFilter generatorFilter = (GeneratorFilter) result;
        assertThat(generatorFilter).isNotNull();

        //
        // Map<String, FilterCondition>
        //
        Map<String, FilterCondition> filters = generatorFilter.filters();
        assertThat(filters).containsKey("FIRST_NAME");

        //
        // FilterCondition
        //
        FilterCondition filterCondition = filters.get("FIRST_NAME");
        assertThat(filterCondition).isNotNull();
        assertThat(filterCondition.field()).isEqualTo(TemplateField.FIRST_NAME);
        assertThat(filterCondition.operator()).isEqualTo(FilterOperator.STARTS_WITH);
        assertThat(filterCondition.filterValue()).startsWith("REN");
    }

    @Test
    public void testGeneratorFilterResolverLastNameEndsWith() throws NoSuchMethodException, SecurityException {
        GeneratorFilterResolver generatorFilterResolver = new GeneratorFilterResolver();

        // MethodParameter
        MethodParameter methodParameter = getMethodParameterForTest(0);

        // NativeWebRequest: Mock the incoming web request dependencies
        Map<String, String[]> paramMap = new LinkedHashMap<>();
        paramMap.put("filter[LAST_NAME][endswith]", new String[] { "T" });
        NativeWebRequest nativeWebRequest = getNativeWebRequestForTest(paramMap);

        // resolveArgument(MethodParameter,ModelAndViewContainer,NativeWebRequest,WebDataBinderFactory)
        Object result = generatorFilterResolver
                .resolveArgument(methodParameter, null, nativeWebRequest, null);

        //
        // GeneratorFilter
        //
        GeneratorFilter generatorFilter = (GeneratorFilter) result;
        assertThat(generatorFilter).isNotNull();

        //
        // Map<String, FilterCondition>
        //
        Map<String, FilterCondition> filters = generatorFilter.filters();
        assertThat(filters).containsKey("LAST_NAME");

        //
        // FilterCondition
        //
        FilterCondition filterCondition = filters.get("LAST_NAME");
        assertThat(filterCondition).isNotNull();
        assertThat(filterCondition.field()).isEqualTo(TemplateField.LAST_NAME);
        assertThat(filterCondition.operator()).isEqualTo(FilterOperator.ENDS_WITH);
        assertThat(filterCondition.filterValue()).endsWith("T");
    }

    @Test
    public void testGeneratorFilterResolverGenderIdentityIn() throws NoSuchMethodException, SecurityException {
        GeneratorFilterResolver generatorFilterResolver = new GeneratorFilterResolver();

        // MethodParameter
        MethodParameter methodParameter = getMethodParameterForTest(0);

        // NativeWebRequest: Mock the incoming web request dependencies
        Map<String, String[]> paramMap = new LinkedHashMap<>();
        paramMap.put("filter[GENDER_IDENTITY][in]", new String[] { "FEMALE" });
        NativeWebRequest nativeWebRequest = getNativeWebRequestForTest(paramMap);

        // resolveArgument(MethodParameter,ModelAndViewContainer,NativeWebRequest,WebDataBinderFactory)
        Object result = generatorFilterResolver
                .resolveArgument(methodParameter, null, nativeWebRequest, null);

        //
        // GeneratorFilter
        //
        GeneratorFilter generatorFilter = (GeneratorFilter) result;
        assertThat(generatorFilter).isNotNull();

        //
        // Map<String, FilterCondition>
        //
        Map<String, FilterCondition> filters = generatorFilter.filters();
        assertThat(filters).containsKey("GENDER_IDENTITY");

        //
        // FilterCondition
        //
        FilterCondition filterCondition = filters.get("GENDER_IDENTITY");
        assertThat(filterCondition).isNotNull();
        assertThat(filterCondition.field()).isEqualTo(TemplateField.GENDER_IDENTITY);
        assertThat(filterCondition.operator()).isEqualTo(FilterOperator.IN);
        assertThat(filterCondition.operator().isEnabled()).isEqualTo(true);
        assertThat(filterCondition.filterValue()).isEqualTo("FEMALE");
    }

    // Dummy method to trigger GeneratorFilter resolver
    public void testControllerMethod(
            GeneratorFilter filters,
            @RequestParam(name="FIRST_NAME", required=false) String firstName
    ) {}

    private Method getMethodForTest() throws NoSuchMethodException, SecurityException {
        Method method = GeneratorFilterResolverTests.class
                .getMethod("testControllerMethod", GeneratorFilter.class, String.class);
        return method;
    }

    private MethodParameter getMethodParameterForTest(
            int parameterIndex
    ) throws NoSuchMethodException, SecurityException {
        Method method = getMethodForTest();
        return new SynthesizingMethodParameter(method, 0);
    }

    private MethodParameter getMethodParameterForTest(Method method, int parameterIndex, int nestingLevel) {
        return new SynthesizingMethodParameter(method, 0);
    }

    private NativeWebRequest getNativeWebRequestForTest(Map<String, String[]> parameterMap) {
        NativeWebRequest nativeWebRequest = Mockito.mock(NativeWebRequest.class);

        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);

        if(parameterMap != null) {
            when(nativeWebRequest.getParameterMap()).thenReturn(parameterMap);
            when(httpServletRequest.getParameterMap()).thenReturn(parameterMap);
        }

        when(nativeWebRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(httpServletRequest);

        return nativeWebRequest;
    }

    //@Test
    void testConverterMultipleFilters() {
        SelectionFilterConverter converter = new SelectionFilterConverter();

        String query =
            "filter[FIRST_NAME][eq]=RENEE&" +
            "filter[LAST_NAME][startswith]=B&" +
            "filter[GENDER_IDENTITY][in]=FEMALE";

        // convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) -> Object
        SelectionFilter filter = (SelectionFilter)converter.convert(query, null, null);

        assertEquals("RENEE", filter.equalToMap().get(TemplateField.FIRST_NAME));
        assertEquals("B", filter.startsWithMap().get(TemplateField.LAST_NAME));

        Set<?> s = filter.inEnumMap().get(TemplateField.GENDER_IDENTITY);
        
        //assertEquals(1, s.size());
    }
}
