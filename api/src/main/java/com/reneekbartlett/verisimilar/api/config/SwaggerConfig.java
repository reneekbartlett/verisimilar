package com.reneekbartlett.verisimilar.api.config;

import java.util.Arrays;
import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;

@Configuration
public class SwaggerConfig {

    private static final List<String> EXCLUDED_PATHS = Arrays.asList("/api/public");

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Verisimilar API").version("1.0.0").description("Data Generator"));
    }

    /**
     * Expose only endpoints that start with /api/generate/ and hide everything else.
     */
    @Bean
    public GroupedOpenApi publicApi() {
        // TODO:  Add /api/generateBulk
        return GroupedOpenApi.builder()
                .group("generate-endpoints") // Label shown in the top-right dropdown of Swagger UI
                .pathsToMatch("/api/generate/**")    // Match pattern: Only include these paths
                .addOperationCustomizer((operation, handlerMethod) -> {
                    Parameter apiKeyHeaderParameter = new Parameter()
                            .name("X-API-Key")
                            .description("Mandatory API key for Swagger Group")
                            .in("header")
                            .required(true)
                            .schema(new StringSchema().example("SwaggerKey123"));
                    operation.addParametersItem(apiKeyHeaderParameter);
                    return operation;
                })
                .build();
    }

//    /**
//     * Inject the X-Api-Key parameter automatically into every API endpoint definition.
//     */
//    @Bean
//    public OperationCustomizer addGlobalHeaderParameter() {
//        return (operation, handlerMethod) -> {
//            String methodPath = getMethodPath(handlerMethod);
//            // Check if the current endpoint path is in our exclusion list
//            if (EXCLUDED_PATHS.stream().anyMatch(methodPath::startsWith)) {
//                return operation; // Skip adding the parameter and return early
//            }
//
//            Parameter apiKeyHeaderParameter = new Parameter()
//                    .name("X-API-Key")
//                    .description("Your unique administrative access token required for all requests")
//                    .in("header") // Options: "header", "query"
//                    .required(true)
//                    .schema(new StringSchema().example("SwaggerKey123"));
//            operation.addParametersItem(apiKeyHeaderParameter);
//            return operation;
//        };
//    }
//
//    /**
//     * Helper method to extract the base URL path mapped to the handler method.
//     */
//    private String getMethodPath(HandlerMethod handlerMethod) {
//        RequestMapping requestMapping = handlerMethod.getMethodAnnotation(RequestMapping.class);
//        if (requestMapping != null && requestMapping.value().length > 0) {
//            return requestMapping.value()[0];
//        }
//
//        // Fallback check if the class level has a mapping (e.g., @RequestMapping("/api/generate"))
//        RequestMapping classMapping = handlerMethod.getBeanType().getAnnotation(RequestMapping.class);
//        if (classMapping != null && classMapping.value().length > 0) {
//            return classMapping.value()[0];
//        }
//
//        return "";
//    }
}
