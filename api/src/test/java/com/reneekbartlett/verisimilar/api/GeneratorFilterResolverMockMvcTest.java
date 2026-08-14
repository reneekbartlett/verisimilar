package com.reneekbartlett.verisimilar.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.reneekbartlett.verisimilar.api.model.GeneratorFilter;
//import com.reneekbartlett.verisimilar.api.config.WebConfig;
import com.reneekbartlett.verisimilar.api.service.GeneratorFilterResolver;
//import com.reneekbartlett.verisimilar.api.service.StringToFilterOperatorConverter;
//import com.reneekbartlett.verisimilar.api.service.StringToTemplateFieldConverter;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import jakarta.servlet.http.HttpServletRequest;

/***
 * @WebMvcTest automatically scans and registers classes implementing Converter, 
 * it will pick up your component seamlessly as long as it is a recognized bean.
 */
@WebMvcTest(
//@SpringBootTest(
    controllers = { 
            GeneratorFilterResolverMockMvcTest.TestFilterController.class
    },
    properties = {
            "application.security.shared-secret=MySecretPassphraseMustBe32Bytes!",
            "application.security.allowUrlApiKeys=true",
            "application.security.apiUsers=test-user",
            "application.security.apiKeys.test-user.key=TestKey123",
            "application.security.apiKeys.test-user.roles=GENERATE",
            "spring.mvc.problemdetails.enabled=true",
            //"spring.http.converters.preferred-json-mapper"
    }//,
    //excludeFilters = @ComponentScan.Filter(
    //    type = FilterType.ASSIGNABLE_TYPE,
    //    classes = { SecurityConfig.class, JwtService.class, JwtAuthProvider.class, 
    //            ApiKeyProperties.class, ApiKeyAuthProvider.class }
    //)
)
//@AutoConfigureMockMvc
@Import({ GeneratorFilterResolver.class })
//@Import(SelectionFilterConditionalConverter.class)
@Disabled
public class GeneratorFilterResolverMockMvcTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorFilterResolverMockMvcTest.class);

    @Autowired
    private MockMvcTester mockMvc;

    @Autowired
    private org.springframework.web.context.WebApplicationContext context;

    @Autowired
    @Qualifier("mvcConversionService") // Ensures you get the web-configured service
    private ConversionService conversionService;

    public record FilterQuery(String rawTerm) {}

    @RestController
    @RequestMapping("/api/test")
    public static class TestFilterController {
        @GetMapping("/generatorFilter")
        public String testGeneratorFilter(
                GeneratorFilter generatorFilter
                //,HttpServletRequest req
        ) {
            //String filterStr = filter.toString();
            //LOGGER.info("filterStr={}", filterStr);
            //return ResponseEntity.ok().body(filterStr);
            return generatorFilter.toString();
        }

        @GetMapping("/selectionFilter")
        public String testSelectionFilter(
                SelectionFilter selectionFilter//, 
                //HttpServletRequest req
        ) {
            //String filterStr = filter.toString();
            //LOGGER.info("filterStr={}", filterStr);
            //return ResponseEntity.ok().body(filterStr);
            return "";
        }
    }

    //@TestConfiguration
    //static class LocalConfig {
    //    @Bean
    //    public FilterQueryProcessor testDataProcessor() {
    //        return new FilterQueryProcessor();
    //    }
    //}

    @TestConfiguration
    public class WebMvcTestConfig implements WebMvcConfigurer {
        //@Override
        //public void addFormatters(FormatterRegistry registry) {
        //    // Register the conditional converter into Spring MVC
        //    //registry.addConverter(new SelectionFilterConditionalConverter());
        //    //registry.addConverter(new BracketFilterConverter());
        //    //registry.addConverter(new GeneratorFilterConverter());
        //}

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            // Append your custom resolver to the active Spring pipeline
            resolvers.add(new GeneratorFilterResolver());
        }
    }

//    @TestConfiguration
//    public static class SecurityConfig {
//        //@Bean
//        //public AuthenticationManager authenticationManager() {
//        //    return new ProviderManager();
//        //}
//
//        @Bean
//        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//            http.csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
//            return http.build();
//        }
//    }

    //@Test
    void debugConversionManually() {
        // 1. Simulate pulling a raw value from request.getParameter("values")
        String mockRequestParam = "filter[LAST_NAME][startswith]=B"; 

        // 2. Define the exact source and target descriptors Spring uses at runtime
        TypeDescriptor sourceType = TypeDescriptor.valueOf(String.class);
        TypeDescriptor targetType = TypeDescriptor.valueOf(SelectionFilter.class);

        // 3. Manually execute the conversion framework
        Object result = conversionService.convert(mockRequestParam, sourceType, targetType);

        // 4. Assert or inspect your result
        assertNotNull(result);
        assertTrue(result instanceof SelectionFilter);
    }

//    @Test
//    void debugWithActualRequestParams() {
//        // 1. Create a mock servlet request and add your query params
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.addParameter("tags", "java,spring,mvc");
//
//        // 2. Create a target command object that has the field you want to bind to
//        // (Assume TargetForm has a 'private StringSetWrapper tags;' property)
//        TargetForm targetForm = new TargetForm(); 
//        ServletRequestDataBinder binder = new ServletRequestDataBinder(targetForm);
//
//        // 3. Attach Spring's web conversion service to the binder
//        binder.setConversionService(conversionService);
//
//        // 4. Trigger the exact binding routine Spring MVC performs
//        binder.bind(request);
//
//        // 5. Inspect if the converter successfully populated the object field
//        System.out.println("Bound Object: " + targetForm.getTags());
//    }

    @Test
    @WithMockUser
    public void testGeneratorFilter() {
        String fullUri = "/api/test/generatorFilter?api_key=TestKey123"
                + "&filter[FIRST_NAME][eq]=RENEE&filter[LAST_NAME][startswith]=B"
                + "&filter[MIDDLE_NAME][endswith]=Y";

        MockHttpServletRequestBuilder reqBldr = MockMvcRequestBuilders
                .get("/api/test/generatorFilter")
                //.param("api_key", "TestKey123")
                .param("filter[FIRST_NAME][eq]", "RENEE")
                .param("filter[LAST_NAME][startswith]", "B")
                .param("filter[MIDDLE_NAME][endswith]", "Y");
        MvcTestResult mvcTestResult = this.mockMvc.perform(reqBldr);
        try {
            String content = mvcTestResult.getResponse().getContentAsString();
            LOGGER.debug("done {}", content);

            //assertThat(mvcTestResult).hasStatusOk();
            assertThat(mvcTestResult).bodyText().contains("RENEE");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //@Test
    public void test1() {

        //var conversionService = context.getBean("mvcConversionService", org.springframework.core.convert.support.ConfigurableConversionService.class);
        // This prints out every converter Spring MVC knows about right now
        //LOGGER.debug("=== REGISTERED CONVERTERS ===");
        //LOGGER.debug("{}", conversionService);

        mockMvc.get()
            .uri("/api/test/generatorFilter?api_key=TestKey123&filter[FIRST_NAME][eq]=RENEE&filter[LAST_NAME][startswith]=B&filter[MIDDLE_NAME][endswith]=Y")//.header("X-API-Key", "TestKey123")
            //.param("filter[LAST_NAME][startswith]", "B")
            .exchange()
            .assertThat()
            .hasStatusOk();
            //.bodyJson().extracting("$.startsWithMap.LAST_NAME").isEqualTo("B");
    }

    //@Test
    public void testStartsWithOperator() throws Exception {

        Map<String, String> params = HashMap.newHashMap(2);
        params.put("filter[FIRST_NAME][eq]", "RENEE");
        params.put("filter[LAST_NAME][startswith]", "B");

        MultiValueMap<String, String> paramsMap = MultiValueMap.fromSingleValue(params);

        MockHttpServletRequestBuilder reqBldr = MockMvcRequestBuilders.get("/api/test/selectionFilter")
                .header("X-API-Key", "TestKey123")
                .params(paramsMap);
                //.param("filter[LAST_NAME][startswith]", "B");

        MvcTestResult mvcTestResult = this.mockMvc.perform(reqBldr);

        //"{"firstName":null,"middleName":null,"lastName":null,"nickName":null,"gender":null,"genders":null,"birthday":null,
        //"generation":null,"minYear":null,"maxYear":null,"streetName":null,"streetSuffix":null,"address2":null,"city":null,
        //"state":null,"states":null,"zipCodes":null,"region":null,"ethnicity":null,"domainType":null,"domain":null,"usernameType":null,
        //"username":null,"areaCode":null,"customPredicate":null,"customPredicates":null,"startsWithMap":null,"endsWithMap":null,"equalToMap":null,
        //"containsMap":null,"inMap":null,"inEnumMap":null,"domains":[],"empty":false,"personRecord":{"fullName":{"firstName":"","middleName":"",
        //"lastName":"","gender":null},"gender":"GENDER_UNSPECIFIED","birthday":null,"postalAddress":null,"emailAddress":null,"phoneNumber":null},
        //"resolvedValues":{}}"
        String content = mvcTestResult.getResponse().getContentAsString();

        //LOGGER.debug("done {}", content);

        assertThat(mvcTestResult).hasStatusOk().bodyText().contains("RENEE");

        //mockMvc.perform(get("/api/test/filter")
        //        .param("filter[LAST_NAME][startswith]", "B"))
        //        .andExpect(status().isOk())
        //        .andExpect(jsonPath("$.startsWithMap.LAST_NAME").value("B"));
    }

    //@Test
    public void testEqOperator() throws Exception {

        MockHttpServletRequestBuilder reqBldr = MockMvcRequestBuilders.get("/api/test/selectionFilter")
                .header("X-API-Key", "TestKey123")
                .param("filter[LAST_NAME][startswith]", "B")
                .param("filter[FIRST_NAME][eq]", "RENEE");
        
        MvcTestResult mvcTestResult = this.mockMvc.perform(reqBldr);

        assertThat(mvcTestResult).hasStatusOk();

        //.andExpect(status().isOk())
        //.andExpect(jsonPath("$.firstName").value("RENEE"));
    }

    //@Test
    void testInOperator() throws Exception {
        MockHttpServletRequestBuilder reqBldr = MockMvcRequestBuilders.get("/api/test/selectionFilter")
                .header("X-API-Key", "TestKey123")
                .param("filter[GENDER_IDENTITY][in]", "FEMALE,NON_BINARY");
        MvcTestResult mvcTestResult = this.mockMvc.perform(reqBldr);
        assertThat(mvcTestResult).hasStatusOk();
        
        //.andExpect(status().isOk())
        //.andExpect(jsonPath("$.genders").isArray())
        //.andExpect(jsonPath("$.genders[0]").value("FEMALE"))
        //.andExpect(jsonPath("$.genders[1]").value("NON_BINARY"));
    }
}
