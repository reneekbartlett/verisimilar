package com.reneekbartlett.verisimilar.api.controller;

import static org.assertj.core.api.Assertions.assertThat;

//import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.when;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.reneekbartlett.verisimilar.api.service.GenerateFullNameService;
import com.reneekbartlett.verisimilar.api.service.GeneratorFilterResolver;
//import com.reneekbartlett.verisimilar.api.service.StringToFilterOperatorConverter;
//import com.reneekbartlett.verisimilar.api.service.StringToTemplateFieldConverter;
import com.reneekbartlett.verisimilar.core.model.FullName;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

/***
 * Components:  ApiKeyProperties, ApiKeyAuthProvider
 * Services:    ApiKeyService
 */

@WebMvcTest(
    controllers = { GenerateFullNameController.class },
    properties = {
        "application.security.shared-secret=MySecretPassphraseMustBe32Bytes!",
        "application.security.allowUrlApiKeys=true",
        "application.security.apiUsers=test-user",
        "application.security.apiKeys.test-user.key=TestKey123",
        "application.security.apiKeys.test-user.roles=GENERATE",
    }
)
@Import({
    //StringToTemplateFieldConverter.class, 
    //StringToFilterOperatorConverter.class,
    GeneratorFilterResolver.class
})
//@Import({
//    SecurityConfig.class, ApiKeyProperties.class, ApiKeyAuthProvider.class, ApiKeyService.class
//})
// TODO:  https://docs.spring.io/spring-security/reference/servlet/test/method.html
//@org.junit.jupiter.api.Disabled
public class GenerateFullNameControllerTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateFullNameControllerTests.class);

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private GenerateFullNameService fullNameService;

    //@Test
    public void generateTest() throws Exception {
//        FullNameGenerator generator = new FullNameGenerator(resolverRegistry.selectors());
//        GenerateFullNameService service = new GenerateFullNameService(generator);
//        GenerateFullNameController controller = new GenerateFullNameController(service);

        SelectionFilter filter = SelectionFilter.builder().build();
        FullName mockFullName = new FullName("RENEE", "K", "BARTLETT");
        when(fullNameService.generate(filter)).thenReturn(mockFullName.toString());

        //Map<String, String[]> params = HashMap.newHashMap(2);
        //params.put("filter[FIRST_NAME][eq]", new String[]{"RENEE"});
        //params.put("filter[LAST_NAME][eq]", new String[]{"BARTLETT"});

        // 1. Prepare target keys matching your bracket pattern
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.put("filter[FIRST_NAME][eq]", List.of("RENEE"));
        queryParams.put("filter[LAST_NAME][startswith]", List.of("B"));
        queryParams.put("filter[GENDER_IDENTITY][in]", List.of("FEMALE"));
        queryParams.put("unrelatedParam", List.of("should_be_ignored")); // Will not map to 'filter'

        //MockHttpServletRequest request = new MockHttpServletRequest();
        //request.addParameters(params);

        //ResponseEntity<Object> r = controller.generate(null, null, null, null, null, null, request);

        //assertThat(mvc.get().uri("/api/generate/fullName").header("X-API-Key", "TestKey123"))
        //    .hasStatusOk()
        //    .hasBodyTextEqualTo("RENEE K BARTLETT");

        MockHttpServletRequestBuilder reqBldr = MockMvcRequestBuilders.get("/api/generate/fullName")
                .header("X-API-Key", "TestKey123")
                .params(queryParams);
                //.param("name", "Bob")
                //.param("age", "25");

        MvcTestResult mvcTestResult = this.mvc.perform(reqBldr);

        String content = mvcTestResult.getResponse().getContentAsString();
        assertThat(content).isNotNull();
        LOGGER.debug("done {}", content);

        
        
        //assertThat(mvcTestResult)
        //    .hasStatusOk()
        //    .bodyText().contains("RENEE");
            // Assert the nested JSON structure returned by the controller
            //.hasPathSatisfying("$.FIRST_NAME.eq", val -> assertThat(val).isEqualTo("RENEE"))
            //.hasPathSatisfying("$.LAST_NAME.startswith", val -> assertThat(val).isEqualTo("B"))
            // Ensure unrelated fields were not captured in the filter map object
            //.doesNotHavePath("$.unrelatedParam");

        

    }

    //@Test
    void testInvalidOperatorReturnsBadRequest() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

        // "invalid_op" does not match any FilterOperator enum literal definitions
        queryParams.put("filter[FIRST_NAME][invalid_op]", List.of("RENEE"));

        //assertThat(this.mvc.get().uri("/api/generate/fullName")
        //        .params(queryParams))
        //        // Spring fails data binding automatically and yields a bad request status
        //        .hasStatus4xxClientError();
    }
}
