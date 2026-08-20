package com.reneekbartlett.verisimilar.api.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.reneekbartlett.verisimilar.api.model.FilterCondition;
import com.reneekbartlett.verisimilar.api.model.FilterOperator;
import com.reneekbartlett.verisimilar.api.model.GeneratorFilter;
import com.reneekbartlett.verisimilar.core.model.TemplateField;

import jakarta.servlet.http.HttpServletRequest;

// Added via WebConfig via addArgumentResolvers
//@Component
public class GeneratorFilterResolver implements HandlerMethodArgumentResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorFilterResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean hasReqParam = parameter.hasParameterAnnotation(RequestParam.class);

        // Triggers this resolver only when the controller method argument is a GeneratorFilter
        return parameter.getParameterType().equals(GeneratorFilter.class);
    }

    @Override
    public GeneratorFilter resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {

        // Unwrap native servlet request to access attributes, headers, or IPs
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            return null;
        }

        // Parent method
        Method method = parameter.getMethod();

        Map<String, String> templateFieldParams = testTemplateFields(method, webRequest);
        LOGGER.debug("templateFieldParams");

        // Fetch the flat parameter map from the request
        Map<String, String[]> parameterMap = request.getParameterMap();

        // Create Map to store FilterCondition(s)
        Map<String, FilterCondition> structuredFilters = HashMap.newHashMap(parameterMap.size());

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = URLDecoder.decode(entry.getKey(), StandardCharsets.UTF_8);
            String[] values = entry.getValue();

            if (values == null || values.length == 0) {
                LOGGER.debug("null/empty values");
                continue;
            }

            String filterValue = values[0]; // Read first parameter value

            if (key.startsWith("filter")) {
                LOGGER.debug("Passes filter check - key={}", key);

                int bracketCount = key.length() - (key.replace("[", "").replace("]", "").length());
                String fieldAndOperator = key.substring(key.indexOf("["), key.lastIndexOf("]")+1);
                //LOGGER.debug("fieldAndOperator={}", fieldAndOperator);

                Pattern bracketPattern = Pattern.compile("^\\[(?<field>[^\\]]+)\\]\\[(?<operator>[^\\]]+)\\]$");
                Matcher bracketMatcher = bracketPattern.matcher(fieldAndOperator);

                // Passes bracket/field+operator format checks?
                if(bracketCount == 4 && bracketMatcher.matches()) {
                    try {
                        // TODO:  Clean up
                        var namedGroupsKeys = bracketMatcher.namedGroups().keySet();
                        int groupCount = bracketMatcher.groupCount();
                        LOGGER.debug("matches splitPattern - key={}; groupCount={}; groups={}", key, groupCount, namedGroupsKeys);

                        String fieldName = bracketMatcher.group("field"); // e.g., "FIRST_NAME"
                        String operator = bracketMatcher.group("operator"); // e.g., "eq"

                        TemplateField templateField = TemplateField.fromValue(fieldName);
                        FilterOperator filterOperator = FilterOperator.fromKeyword(operator);

                        if(templateField == null) {
                            LOGGER.warn("invalid template field: {}", fieldName);
                            continue;
                        }

                        if(filterOperator == null || !filterOperator.isEnabled()) {
                            // TODO: throw error?
                            LOGGER.warn("invalid/disabled filter operator for {}: {}", fieldName, operator);
                            continue;
                        }

                        // TODO: Validate (syntax, characters, etc.)
                        if(filterValue == null) {
                            LOGGER.warn("invalid filter value for [{}][{}]: {}", fieldName, operator, filterValue);
                            continue;
                        }

                        structuredFilters.put(fieldName, new FilterCondition(templateField, filterOperator, filterValue));

                    } catch (IndexOutOfBoundsException | IllegalStateException e) {
                        LOGGER.error("Error parsing key.  key={}, message={}", key, e.getMessage(), e);
                    }
                } else {
                    LOGGER.debug("doesn't match splitPattern - key={}", key);
                }
            } else {
                LOGGER.debug("Fails filter check - key={}", key);

                // TODO Check for @RequestParam values?
                //key
            }
        }

        // TODO:  Read/Store request params or other metadata
        // String clientIp = request.getRemoteAddr();

        return new GeneratorFilter(structuredFilters);
    }

    private Map<String, String> testTemplateFields(Method method, NativeWebRequest webRequest){
        if (method == null) {
            return null; // Return default or empty fallback
        }

        Map<String, String> requestParamValues = new HashMap<>();
        int totalParameters = method.getParameterCount();

        for (int i = 0; i < totalParameters; i++) {
            MethodParameter siblingParam = new MethodParameter(method, i);
            Class<?> targetType = siblingParam.getParameterType();

            // Look for @RequestParam annotations
            RequestParam requestParamAnnotation = siblingParam.getParameterAnnotation(RequestParam.class);

            if (requestParamAnnotation != null) {
                // Determine the key name used in the query string (checking name or value)
                String paramName = requestParamAnnotation.name().isEmpty() 
                        ? requestParamAnnotation.value() : requestParamAnnotation.name();

                // If it's still empty, fall back to the actual Java parameter variable name
                if (paramName.isEmpty()) {
                    siblingParam.initParameterNameDiscovery(new DefaultParameterNameDiscoverer());
                    paramName = siblingParam.getParameterName();
                }

                // 3. Pull the runtime value off the incoming request
                String paramValue = webRequest.getParameter(paramName);

                if (paramValue != null) {
                    requestParamValues.put(paramName, paramValue);

                    // Dynamically cast the string to whatever type the controller parameter requests!
                    Object convertedValue = webRequest.getAttribute(paramName, NativeWebRequest.SCOPE_REQUEST); 

                    // Alternatively, inject ConversionService into your resolver to do it programmatically:
                    // Object convertedValue = conversionService.convert(rawValue, targetType);
                }
            }
        }

        return requestParamValues;
    }
}
