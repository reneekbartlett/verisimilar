package com.reneekbartlett.verisimilar.core.generator;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

/***
 * 
 */
// TODO:  Add tests for invalid values
public class BirthdayGeneratorTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(BirthdayGeneratorTests.class);

    @Test
    public void GeneratedBirthday_ShouldBeBetweenDefaults() {
        BirthdayGenerator birthdayGenerator = new BirthdayGenerator();
        LocalDate birthday = birthdayGenerator.generate();

        LOGGER.debug("birthday=" + birthday + ", DEFAULT_MIN_YEAR=" + BirthdayGenerator.DEFAULT_MIN_YEAR + 
                ", DEFAULT_MAX_YEAR=" + BirthdayGenerator.DEFAULT_MAX_YEAR);
        Assertions.assertNotNull(birthday);
        Assertions.assertTrue(birthday.getYear() >= BirthdayGenerator.DEFAULT_MIN_YEAR, 
                "Should be between " + BirthdayGenerator.DEFAULT_MIN_YEAR + " and " + BirthdayGenerator.DEFAULT_MAX_YEAR +"");
        Assertions.assertTrue(birthday.getYear() <= BirthdayGenerator.DEFAULT_MAX_YEAR, 
                "Should be between " + BirthdayGenerator.DEFAULT_MIN_YEAR + " and " + BirthdayGenerator.DEFAULT_MAX_YEAR +"");
    }

    @Test
    public void GeneratedBirthday_YearCriteria_ShouldBeBetweenMinAndMax() {
        BirthdayGenerator birthdayGenerator = new BirthdayGenerator();

        int minYear = 1985;
        int maxYear = 1995;
        SelectionFilter filter = SelectionFilter.builder()
                .minYear(minYear).maxYear(maxYear).build();

        LocalDate birthday = birthdayGenerator.generate(filter);

        LOGGER.debug("birthday=" + birthday + ", minYear=" + minYear + 
                ", maxYear=" + maxYear);
        Assertions.assertTrue(birthday.getYear() >= minYear, "Should be between " + minYear + " and " + maxYear +"");
        Assertions.assertTrue(birthday.getYear() <= maxYear, "Should be between " + minYear + " and " + maxYear +"");
    }
}
