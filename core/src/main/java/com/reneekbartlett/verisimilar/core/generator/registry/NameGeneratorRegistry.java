package com.reneekbartlett.verisimilar.core.generator.registry;

import com.reneekbartlett.verisimilar.core.generator.FirstNameGenerator;
import com.reneekbartlett.verisimilar.core.generator.MiddleNameGenerator;
import com.reneekbartlett.verisimilar.core.generator.LastNameGenerator;

public class NameGeneratorRegistry {

    private final FirstNameGenerator firstNameGenerator;
    private final MiddleNameGenerator middleNameGenerator;
    private final LastNameGenerator lastNameGenerator;

    public NameGeneratorRegistry(
            FirstNameGenerator firstNameGenerator,
            MiddleNameGenerator middleNameGenerator,
            LastNameGenerator lastNameGenerator
    ) {
        this.firstNameGenerator = firstNameGenerator;
        this.middleNameGenerator = middleNameGenerator;
        this.lastNameGenerator = lastNameGenerator;
    }

    public FirstNameGenerator first() {
        return firstNameGenerator;
    }

    public MiddleNameGenerator middle() {
        return middleNameGenerator;
    }
    
    public LastNameGenerator last() {
        return lastNameGenerator;
    }
}
