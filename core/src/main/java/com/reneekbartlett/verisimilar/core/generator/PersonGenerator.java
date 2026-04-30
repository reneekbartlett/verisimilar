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
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;

public class PersonGenerator extends AbstractValueGenerator<PersonRecord>{

    private final BirthdayGenerator birthdayGenerator;
    private final FullNameGenerator fullNameGenerator;
    private final PostalAddressRecordGenerator postalAddressGenerator;
    private final PhoneNumberGenerator phoneNumberGenerator;
    private final EmailAddressGenerator emailAddressGenerator;

    public PersonGenerator(
            BirthdayGenerator birthdayGenerator, 
            FullNameGenerator fullNameGenerator,
            PostalAddressRecordGenerator postalAddressGenerator,
            PhoneNumberGenerator phoneNumberGenerator,
            EmailAddressGenerator emailAddressGenerator
    ) {
        this.birthdayGenerator = birthdayGenerator;
        this.fullNameGenerator = fullNameGenerator;
        this.postalAddressGenerator = postalAddressGenerator;
        this.phoneNumberGenerator = phoneNumberGenerator;
        this.emailAddressGenerator = emailAddressGenerator;
    }

    public PersonGenerator(DatasetSelectionEngineRegistry selectors){
        this(
            new BirthdayGenerator(),
            new FullNameGenerator(selectors),
            new PostalAddressRecordGenerator(selectors),
            new PhoneNumberGenerator(selectors),
            new EmailAddressGenerator(selectors)
        );
    }

    public PersonGenerator(DatasetResolverRegistry resolvers){
        this(
            new BirthdayGenerator(),
            new FullNameGenerator(resolvers.selectors()),
            new PostalAddressRecordGenerator(resolvers.selectors()),
            new PhoneNumberGenerator(resolvers.selectors()),
            new EmailAddressGenerator(resolvers.selectors())
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

    private LocalDate generateBirthday(DatasetResolutionContext ctx, SelectionFilter filter) {
        return birthdayGenerator.generate(ctx, filter);
    }

    private FullName generateFullName(DatasetResolutionContext ctx, SelectionFilter filter) {
        return fullNameGenerator.generateValue(ctx, filter);
    }

    private PostalAddress generatePostalAddress(DatasetResolutionContext ctx, SelectionFilter filter) {
        return postalAddressGenerator.generate(ctx, filter);
    }

    private PhoneNumber generatePhoneNumber(DatasetResolutionContext ctx, SelectionFilter filter) {
        return this.phoneNumberGenerator.generate(ctx, filter);
    }

    private EmailAddressRecord generateEmailAddress(DatasetResolutionContext ctx, SelectionFilter filter) {
        return this.emailAddressGenerator.generate(ctx, filter);
    }

    @Override
    protected Class<PersonRecord> valueType() {
        return PersonRecord.class;
    }
}
