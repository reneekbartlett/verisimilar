package com.reneekbartlett.verisimilar.core.generator;

import java.time.LocalDate;
import java.util.Set;

import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.generator.api.AbstractValueGenerator;

import com.reneekbartlett.verisimilar.core.model.EmailAddressRecord;
import com.reneekbartlett.verisimilar.core.model.FullName;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.PersonRecord;
import com.reneekbartlett.verisimilar.core.model.PhoneNumber;
import com.reneekbartlett.verisimilar.core.model.PostalAddress;
import com.reneekbartlett.verisimilar.core.model.USState;

import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.engine.AddressTwoSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.AreaCodeSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.CityStateZipSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.FirstNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.KeywordSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.LastNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.MiddleNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetSuffixSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;

public class PersonGenerator extends AbstractValueGenerator<PersonRecord>{

    private final BirthdayGenerator birthdayGenerator;
    private final FullNameGenerator fullNameGenerator;
    private final PostalAddressRecordGenerator postalAddressGenerator;
    private final PhoneNumberGenerator phoneNumberGenerator;
    private final EmailAddressGenerator emailAddressGenerator;

    public PersonGenerator(DatasetResolverRegistry resolvers){
        this.birthdayGenerator = new BirthdayGenerator();

        this.fullNameGenerator = new FullNameGenerator(
                new FirstNameSelectionEngine(resolvers),
                new MiddleNameSelectionEngine(resolvers),
                new LastNameSelectionEngine(resolvers)
        );

        this.postalAddressGenerator = new PostalAddressRecordGenerator(
                new StreetNameSelectionEngine(resolvers), 
                new StreetSuffixSelectionEngine(resolvers),
                new AddressTwoSelectionEngine(resolvers),
                new CityStateZipSelectionEngine(resolvers)
        );

        this.phoneNumberGenerator = new PhoneNumberGenerator(
                new AreaCodeSelectionEngine(resolvers)
        );

        this.emailAddressGenerator = new EmailAddressGenerator(
                new UsernameSelectionEngine(resolvers),
                new DomainSelectionEngine(resolvers),
                new KeywordSelectionEngine(resolvers)
        );
    }

    @Override
    protected PersonRecord generateValue(DatasetResolutionContext ctx, SelectionFilter criteria){
        return generatePerson(ctx, criteria);
    }

    // TODO:  Make these asynchronous
    private PersonRecord generatePerson(DatasetResolutionContext ctx, SelectionFilter filter) {
        // Generate the birthday first.
        LocalDate birthday = generateBirthday(ctx, filter);

        GenderIdentity gender = filter.gender().orElseGet(this::generateGenderIdentity);

        // Then the Postal.
        PostalAddress postalAddress = generatePostalAddress(ctx, filter);

        // Use information to create name
        USState state = USState.fromAbbreviation(postalAddress.state());
        //DatasetResolutionContext.Builder stepTwoConstraint = DatasetResolutionContext.builder().states(Set.of(state)).gender(gender);
        SelectionFilter.Builder stepTwoCriteria = SelectionFilter.builder()
                .birthday(birthday)
                .states(Set.of(state))
                .gender(gender);

        //FullName fullName = generateFullName(stepTwoConstraint.build(), stepTwoCriteria.build());
        FullName fullName = generateFullName(ctx, stepTwoCriteria.build());

        //PhoneNumber phoneNumber = generatePhoneNumber(stepTwoConstraint.build(), stepTwoCriteria.build());
        PhoneNumber phoneNumber = generatePhoneNumber(ctx, stepTwoCriteria.build());

        //
        // TODO:  These 3 could probably be async
        //

        // TODO: Add Birthday and Postal to weight name
        //DatasetResolutionContext.Builder stepThreeConstraint = DatasetResolutionContext.builder().gender(gender);
        SelectionFilter.Builder stepThreeCriteria = SelectionFilter.builder()
                .firstName(fullName.firstName())
                .lastName(fullName.lastName())
                .middleName(fullName.middleName())
                .birthday(birthday)
                .gender(gender);
        EmailAddressRecord emailAddress = generateEmailAddress(ctx, stepThreeCriteria.build());

        return new PersonRecord(fullName, gender, birthday, postalAddress, emailAddress, phoneNumber);
    }

    private GenderIdentity generateGenderIdentity() {
        RandomSelector<GenderIdentity> selector = new WeightedSelectorImpl<>(GenderIdentity.defaultMap());
        return selector.select();
    }

    private LocalDate generateBirthday(DatasetResolutionContext ctx, SelectionFilter criteria) {
        return birthdayGenerator.generate(ctx, criteria);
    }

    private FullName generateFullName(DatasetResolutionContext ctx, SelectionFilter criteria) {
        return fullNameGenerator.generateValue(ctx, criteria);
    }

    private PostalAddress generatePostalAddress(DatasetResolutionContext ctx, SelectionFilter filter) {
        return postalAddressGenerator.generate(ctx, filter);
    }

    private PhoneNumber generatePhoneNumber(DatasetResolutionContext ctx, SelectionFilter criteria) {
        return this.phoneNumberGenerator.generate(ctx, criteria);
    }

    private EmailAddressRecord generateEmailAddress(DatasetResolutionContext ctx, SelectionFilter criteria) {
        return this.emailAddressGenerator.generate(ctx, criteria);
    }

    @Override
    protected Class<PersonRecord> valueType() {
        return PersonRecord.class;
    }
}
