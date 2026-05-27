package com.reneekbartlett.verisimilar.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;

import com.reneekbartlett.verisimilar.api.util.JsonApiParser;
import com.reneekbartlett.verisimilar.api.util.JsonApiParser.FilterConditions;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class JsonApiParserTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonApiParserTests.class);

    @Test
    public void parseTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        Map<String, String[]> params = HashMap.newHashMap(2);
        params.put("filter[FIRST_NAME][startswith]", new String[]{"R"});
        params.put("filter[LAST_NAME][startswith]", new String[]{"B"});
        params.put("filter[STATE][eq]", new String[]{"MA"});

        request.addParameters(params);

        FilterConditions filters = JsonApiParser.parse(request.getParameterMap());

        LOGGER.debug("filters:{}", filters.size());

        SelectionFilter selectionFilter = filters.toSelectionFilterBuilder().build();

        LOGGER.debug("selectionFilter:{}", selectionFilter);

    }

    @Test
    public void tokenizeTest() {
        String input = "filter[author.name][eq]";
        List<String> tokens = JsonApiParser.tokenize(input);

        // Result: ["filter", "author.name", "eq"]
        LOGGER.debug("tokens:{}", tokens);
    }
}
