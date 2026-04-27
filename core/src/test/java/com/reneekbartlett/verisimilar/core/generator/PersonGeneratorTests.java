package com.reneekbartlett.verisimilar.core.generator;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.PersonRecord;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class PersonGeneratorTests {
    //PersonGenerator(DatasetResolverRegistry resolvers)
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonGeneratorTests.class);

    //@Test
    public void GenerateFullName_RandomGender() {
        DatasetResolverRegistry resolvers = TestUtils.getDatasetResolverRegistry();

        PersonGenerator personGenerator = new PersonGenerator(resolvers);

        PersonRecord person = personGenerator.generate();
        LOGGER.debug("person=" + person.toString());
        Assertions.assertNotNull(person);

        //Assertions.assertTrue(fullName.firstName() != fullName.middleName());
    }

    @Test
    public void GeneratePerson_MA_Female() {
        DatasetResolverRegistry resolvers = TestUtils.getDatasetResolverRegistry();
        PersonGenerator personGenerator = new PersonGenerator(resolvers);

        Set<USState> states1 = Set.of(USState.MA);
        SelectionFilter filter1 = SelectionFilter.builder()
                .gender(GenderIdentity.FEMALE)
                .states(states1)
                .zipCodes(Set.of("01545"))
                .build();

        //String areaCodesStrForMA = "339|351|413|508|617|774|781|857|978";
        //List<String> areaCodesForMA = Arrays.asList(areaCodesStrForMA.split("\\|"));

        PersonRecord person1 = personGenerator.generate(filter1);
        LOGGER.debug("person1=" + person1.toString());
        Assertions.assertNotNull(person1);

        SelectionFilter filter2 = SelectionFilter.builder()
                .gender(GenderIdentity.MALE).states(Set.of(USState.MA)).build();
        PersonRecord person2 = personGenerator.generate(filter2);
        LOGGER.debug("person2=" + person2.toString());
        Assertions.assertNotNull(person2);
        
        //Assertions.assertTrue(fullName.firstName() != fullName.middleName());
    }
}
