package com.reneekbartlett.verisimilar.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import com.reneekbartlett.verisimilar.api.security.api.ApiKeyProperties;
import com.reneekbartlett.verisimilar.api.security.config.SecurityConfig;
import com.reneekbartlett.verisimilar.api.service.GenerateFullNameService;
import com.reneekbartlett.verisimilar.core.model.FullName;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/***
 * Components:  ApiKeyProperties, ApiKeyAuthProvider
 * Services:    ApiKeyService
 */

@WebMvcTest(controllers = GenerateFullNameController.class)
@Import({SecurityConfig.class, ApiKeyProperties.class})
// TODO:  https://docs.spring.io/spring-security/reference/servlet/test/method.html
//@Disabled
public class GenerateFullNameControllerTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateFullNameControllerTests.class);

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private GenerateFullNameService fullNameService;

    @Test
    public void generateTest() {
//        FullNameGenerator generator = new FullNameGenerator(resolverRegistry.selectors());
//        GenerateFullNameService service = new GenerateFullNameService(generator);
//        GenerateFullNameController controller = new GenerateFullNameController(service);

        SelectionFilter filter = SelectionFilter.builder().build();
        FullName mockFullName = new FullName("RENEE", "K", "BARTLETT");
        when(fullNameService.generate(filter)).thenReturn(mockFullName.toString());

        Map<String, String[]> params = HashMap.newHashMap(2);
        params.put("filter[FIRST_NAME][eq]", new String[]{"RENEE"});
        params.put("filter[LAST_NAME][eq]", new String[]{"BARTLETT"});

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameters(params);

        //ResponseEntity<Object> r = controller.generate(null, null, null, null, null, null, request);

        assertThat(mvc.get().uri("/api/generate/fullname"))
            .hasStatusOk()
            .hasBodyTextEqualTo("RENEE K BARTLETT");
            //.hasBodyTextEqualTo("{\"id\":101,\"name\":\"Spring Boot 4 Book\"}");
            //.extractingPath("$.name").isEqualTo("Spring Boot 4 Book");

        LOGGER.debug("done");

    }
}
