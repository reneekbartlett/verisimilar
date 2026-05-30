package com.reneekbartlett.verisimilar.core.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.PersonRecord;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class AsyncPersonGeneratorTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncPersonGeneratorTests.class);

    //private final Executor executor;

    public AsyncPersonGeneratorTests() {
        //this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    //@Test
    public void GeneratePersonAsync_Test() {
        AsyncPersonGenerator generator = new AsyncPersonGenerator(TestUtils.getDatasetSelectionEngineRegistry());
        PersonRecord person = generator.generate();
        LOGGER.debug("Generated person: {}", person);
    }

    @Test
    public void GeneratePersonAsync_Test_NameFilter() {
        AsyncPersonGenerator asyncGenerator = new AsyncPersonGenerator(TestUtils.getDatasetSelectionEngineRegistry());
        
        String lastName = "BARTLETT";
        String city = "SHREWSBURY";

        SelectionFilter filter = SelectionFilter.builder()
                .lastName(lastName)
                .startsWith("T", TemplateField.FIRST_NAME)
                .city("SHREWSBURY")
                .build();
        PersonRecord person = asyncGenerator.generate(filter);
        LOGGER.debug("Generated person: {}", person);

        Assertions.assertTrue(person.lastName().equalsIgnoreCase(lastName));
        Assertions.assertTrue(person.city().equalsIgnoreCase(city));
        Assertions.assertTrue(person.firstName().toUpperCase().startsWith("T"));
    }

    @Test
    public void GeneratePersonAsync_Test_PostalFilters() {
        AsyncPersonGenerator asyncGenerator = new AsyncPersonGenerator(TestUtils.getDatasetSelectionEngineRegistry());

        GenderIdentity gender = GenderIdentity.MALE;
        String firstName = "BOB";
        String lastNameChars = "BR";
        String streetName = "MAIN";
        String domain = "gmail.com";

        SelectionFilter filter = SelectionFilter.builder()
                .gender(gender)
                .firstName(firstName)
                .startsWith(lastNameChars, TemplateField.LAST_NAME)
                .streetName(streetName)
                .domain(domain)
                .build();
        PersonRecord person = asyncGenerator.generate(filter);
        LOGGER.debug("Generated person: {}", person);

        Assertions.assertTrue(person.firstName().equalsIgnoreCase(firstName));
        Assertions.assertTrue(person.lastName().toUpperCase().startsWith(lastNameChars));
        
        Assertions.assertTrue(person.emailAddress().domain().equalsIgnoreCase(domain));
        
    }
}
