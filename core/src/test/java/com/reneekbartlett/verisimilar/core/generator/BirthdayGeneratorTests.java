package com.reneekbartlett.verisimilar.core.generator;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

/***
 * 
 */
// TODO:  Add tests for invalid values
@DisabledIf(value = "com.reneekbartlett.verisimilar.core.TestUtils#isCoreTestingDisabled")
public class BirthdayGeneratorTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(BirthdayGeneratorTests.class);

    @Test
    public void GeneratedBirthday_ShouldBeBetweenDefaults() {
        BirthdayGenerator birthdayGenerator = new BirthdayGenerator();

        LocalDate birthday = birthdayGenerator.generate();
        LOGGER.debug("birthday=" + birthday + ", DEFAULT_MIN_YEAR=" + BirthdayGenerator.DEFAULT_MIN_YEAR + 
                ", DEFAULT_MAX_YEAR=" + BirthdayGenerator.DEFAULT_MAX_YEAR);
        Assertions.assertThat(birthday).isNotNull();

        Assertions.assertThat(birthday.getYear()).isGreaterThanOrEqualTo(BirthdayGenerator.DEFAULT_MIN_YEAR)
            .isLessThanOrEqualTo(BirthdayGenerator.DEFAULT_MAX_YEAR);
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
        
        Assertions.assertThat(birthday.getYear()).isGreaterThanOrEqualTo(minYear).isLessThanOrEqualTo(maxYear);
    }
}
