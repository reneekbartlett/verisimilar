package com.reneekbartlett.verisimilar.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MockMvcTester.MockMvcRequestBuilder;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.reneekbartlett.verisimilar.api.dto.PersonResponseDto;
import com.reneekbartlett.verisimilar.api.security.ApiKeyAuthProvider;
import com.reneekbartlett.verisimilar.api.security.ApiKeyProperties;
import com.reneekbartlett.verisimilar.api.security.config.SecurityConfig;
import com.reneekbartlett.verisimilar.api.security.service.ApiKeyService;
import com.reneekbartlett.verisimilar.api.service.GeneratePersonService;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

/***
 * Components:  ApiKeyProperties, ApiKeyAuthProvider
 * Services:    ApiKeyService
 */

@WebMvcTest(
    controllers = { GeneratePersonController.class },
    properties = {
        "application.security.shared-secret=MySecretPassphraseMustBe32Bytes!",
        "application.security.allowUrlApiKeys=true",
        "application.security.apiUsers=test-user",
        "application.security.apiKeys.test-user.key=TestKey123",
        "application.security.apiKeys.test-user.roles=GENERATE",
    }
)
@Import({
    SecurityConfig.class, ApiKeyProperties.class, ApiKeyAuthProvider.class, ApiKeyService.class
})
// TODO:  https://docs.spring.io/spring-security/reference/servlet/test/method.html
//@org.junit.jupiter.api.Disabled
public class GeneratePersonControllerTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratePersonControllerTests.class);

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private GeneratePersonService personService;

    //@Test
    public void generatePersonTest() throws Exception {
//        FullNameGenerator generator = new FullNameGenerator(resolverRegistry.selectors());
//        GenerateFullNameService service = new GenerateFullNameService(generator);
//        GenerateFullNameController controller = new GenerateFullNameController(service);

        SelectionFilter filter = SelectionFilter.builder().build();
        //FullName mockFullName = new FullName("RENEE", "K", "BARTLETT");
        PersonResponseDto mockPerson = new PersonResponseDto();
        when(personService.generate(filter)).thenReturn(mockPerson);

        Map<String, String[]> params = HashMap.newHashMap(2);
        params.put("filter[FIRST_NAME][eq]", new String[]{"RENEE"});
        params.put("filter[LAST_NAME][eq]", new String[]{"BARTLETT"});

        //MockHttpServletRequest request = new MockHttpServletRequest();
        //request.addParameters(params);

        //ResponseEntity<Object> r = controller.generate(null, null, null, null, null, null, request);

        //MvcResult mvcResult = mockMvc.perform(get("/api/generate/person?api_key=VALID_KEY").header("X-API-Key", "VALID_KEY"))
        //        .andExpect(status().isOk())
        //        .andReturn();

        MockMvcRequestBuilder reqBuilder = mvc.get().uri("/api/generate/person").header("X-API-Key", "TestKey123");

        //ResultActions actions = mvc.perform(MockMvcRequestBuilders.get("/api/users")); 
        MvcTestResult actions = mvc.perform(MockMvcRequestBuilders.get("/api/users")); 

        MvcResult result = actions.getMvcResult();
        String responseContent = result.getResponse().getContentAsString();

        assertThat(reqBuilder).hasStatusOk().hasBodyTextEqualTo("RENEE K BARTLETT");

        //assertThat(mvc.get().uri("/api/generate/person"))
        //    .hasStatusOk()
        //    .hasBodyTextEqualTo("RENEE K BARTLETT");

        LOGGER.debug("done {}", responseContent);

    }
}
