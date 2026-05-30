package com.reneekbartlett.verisimilar.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;

import com.reneekbartlett.verisimilar.api.util.JsonApiParser;
import com.reneekbartlett.verisimilar.api.util.JsonApiParser.FilterConditions;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class JsonApiParserTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonApiParserTests.class);

    @Test
    public void parseFilterStartsWithTest() {
        Map<String, String[]> params = HashMap.newHashMap(2);
        params.put("filter[FIRST_NAME][startswith]", new String[]{"R"});
        params.put("filter[LAST_NAME][startswith]", new String[]{"B"});
        params.put("filter[STATE][eq]", new String[]{"MA"});

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameters(params);

        List<TemplateField> filterFields = List.of(TemplateField.FIRST_NAME, 
                TemplateField.MIDDLE_NAME, 
                TemplateField.LAST_NAME, 
                TemplateField.STATE, TemplateField.GENDER_IDENTITY);
        FilterConditions filters = JsonApiParser.parse(request.getParameterMap(), filterFields);

        LOGGER.debug("filters:{}", filters.size());

        SelectionFilter selectionFilter = filters.toSelectionFilterBuilder().build();

        LOGGER.debug("selectionFilter:{}", selectionFilter);

        Assertions.assertEquals(2, selectionFilter.startsWithMap().size());
        Assertions.assertEquals("R", selectionFilter.startsWithMap().get(TemplateField.FIRST_NAME));
        Assertions.assertEquals("B", selectionFilter.startsWithMap().get(TemplateField.LAST_NAME));
    }

    @Test
    public void parseFilterInTest() {
        Map<String, String[]> params = HashMap.newHashMap(2);
        params.put("filter[FIRST_NAME][in]", new String[]{"RENEE,MACKENZIE"});
        params.put("filter[LAST_NAME][in]", new String[]{"BARTLETT,SMITH,Jones"});

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameters(params);

        List<TemplateField> filterFields = List.of(TemplateField.FIRST_NAME, TemplateField.MIDDLE_NAME, TemplateField.LAST_NAME);
        FilterConditions filters = JsonApiParser.parse(request.getParameterMap(), filterFields);
        SelectionFilter selectionFilter = filters.toSelectionFilterBuilder().build();
        LOGGER.debug("filters:{}", filters.size());
        LOGGER.debug("selectionFilter:{}", selectionFilter);

        Assertions.assertEquals(2, selectionFilter.inMap().size());
        Assertions.assertEquals(2, selectionFilter.inMap().get(TemplateField.FIRST_NAME).size());
        Assertions.assertEquals(3, selectionFilter.inMap().get(TemplateField.LAST_NAME).size());
    }

    @Test
    @Disabled
    public void tokenizeTest() {
        String input = "filter[author.name][eq]";
        List<String> tokens = JsonApiParser.tokenize(input);

        // Result: ["filter", "author.name", "eq"]
        LOGGER.debug("tokens:{}", tokens);
    }
}
