package com.reneekbartlett.verisimilar.core.generator;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.generator.api.AbstractValueGenerator;
import com.reneekbartlett.verisimilar.core.model.EmailAddressRecord;
import com.reneekbartlett.verisimilar.core.model.FullName;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.PersonRecord;
import com.reneekbartlett.verisimilar.core.model.PhoneNumber;
import com.reneekbartlett.verisimilar.core.model.PostalAddress;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class AsyncPersonGenerator extends AbstractValueGenerator<PersonRecord>{
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncPersonGenerator.class);

    private final Executor executor;

    private final BirthdayGenerator birthdayGenerator;
    private final GenderIdentityGenerator genderIdentityGenerator;
    private final FullNameGenerator fullNameGenerator;
    private final PostalAddressRecordGenerator postalAddressGenerator;
    private final EmailAddressGenerator emailAddressGenerator;
    private final PhoneNumberGenerator phoneNumberGenerator;

    public AsyncPersonGenerator(
            BirthdayGenerator birthdayGenerator, 
            GenderIdentityGenerator genderIdentityGenerator, 
            FullNameGenerator fullNameGenerator,
            PostalAddressRecordGenerator postalAddressGenerator,
            PhoneNumberGenerator phoneNumberGenerator,
            EmailAddressGenerator emailAddressGenerator
    ) {
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.birthdayGenerator = birthdayGenerator;
        this.genderIdentityGenerator = genderIdentityGenerator;
        this.fullNameGenerator = fullNameGenerator;
        this.postalAddressGenerator = postalAddressGenerator;
        this.phoneNumberGenerator = phoneNumberGenerator;
        this.emailAddressGenerator = emailAddressGenerator;
    }

    public AsyncPersonGenerator(DatasetSelectionEngineRegistry datasetSelectionEngineRegistry) {
        this(
            new BirthdayGenerator(),
            new GenderIdentityGenerator(),
            new FullNameGenerator(datasetSelectionEngineRegistry),
            new PostalAddressRecordGenerator(datasetSelectionEngineRegistry),
            new PhoneNumberGenerator(datasetSelectionEngineRegistry),
            new EmailAddressGenerator(datasetSelectionEngineRegistry)
        );
    }

    public AsyncPersonGenerator(DatasetResolverRegistry resolvers){
        this(
            new BirthdayGenerator(),
            new GenderIdentityGenerator(),
            new FullNameGenerator(resolvers.selectors()),
            new PostalAddressRecordGenerator(resolvers.selectors()),
            new PhoneNumberGenerator(resolvers.selectors()),
            new EmailAddressGenerator(resolvers.selectors())
        );
    }

    @Override
    protected PersonRecord generateValue(DatasetResolutionContext context, SelectionFilter filter) {
        PersonRecord person = generatePersonAsync(context, filter).join();
        LOGGER.debug("Generated person: {}", person);
        return person;
    }

    public CompletableFuture<PersonRecord> generateValueAsync(DatasetResolutionContext context, SelectionFilter filter) {
        return this.generatePersonAsync(context, filter).exceptionally(ex -> { LOGGER.error("ERROR", ex); return null; });
    }

    private CompletableFuture<PersonRecord> generatePersonAsync(DatasetResolutionContext context, SelectionFilter filter) {
        // ------------------------------------------------------------
        // 1) Generate birthday + gender (parallel)
        // ------------------------------------------------------------
        CompletableFuture<LocalDate> birthdayFuture = CompletableFuture
                .supplyAsync(() -> birthdayGenerator.generate(filter), executor);

        CompletableFuture<GenderIdentity> genderFuture = CompletableFuture
                .supplyAsync(() -> genderIdentityGenerator.generate(filter), executor);

        // ------------------------------------------------------------
        // 2) Generate postal address (birthday + gender)
        // ------------------------------------------------------------
        CompletableFuture<PostalAddress> addressFuture = birthdayFuture.thenCombineAsync(genderFuture,
                (birthday, gender) -> generatePostalAddressAsync(filter, birthday, gender), executor);

        // ------------------------------------------------------------
        // 3A) Generate phone number (address)
        // ------------------------------------------------------------
        CompletableFuture<PhoneNumber> phoneFuture = addressFuture
                .thenApplyAsync((postalAddress) -> generatePhoneNumberAsync(filter, postalAddress), executor);

        // ------------------------------------------------------------
        // 3B) Generate names (birthday + gender + address)
        // ------------------------------------------------------------
        CompletableFuture<FullName> nameFuture = birthdayFuture.thenCombineAsync(genderFuture,
                (birthday, gender) -> generateFullNameAsync(filter, birthday, gender), executor);

        // ------------------------------------------------------------
        // 4) Generate email (name + birthday + gender + address)
        // ------------------------------------------------------------
        CompletableFuture<EmailAddressRecord> emailFuture = nameFuture
                .thenCombineAsync(birthdayFuture, (name, birthday) -> new Object[] { name, birthday }, executor)
                .thenCombineAsync(genderFuture, (arr, gender) -> new Object[] { arr[0], arr[1], gender }, executor)
                .thenCombineAsync(addressFuture, (arr, address) -> new Object[] { arr[0], arr[1], arr[2], address }, executor)
                .thenApplyAsync(arr -> {
                    FullName name = (FullName) arr[0];
                    LocalDate birthday = (LocalDate) arr[1];
                    GenderIdentity gender = (GenderIdentity) arr[2];
                    PostalAddress address = (PostalAddress) arr[3];
                    return generateEmailAddressAsync(filter, name, birthday, gender, address);
                }, executor);

        return CompletableFuture
                .allOf(birthdayFuture, genderFuture, addressFuture, phoneFuture, nameFuture, emailFuture)
                .thenApplyAsync(v -> new PersonRecord(nameFuture.join(), genderFuture.join(), birthdayFuture.join(),
                        addressFuture.join(), emailFuture.join(), phoneFuture.join()), executor);
    }

    private PostalAddress generatePostalAddressAsync(SelectionFilter filter, LocalDate birthday, GenderIdentity gender) {
        SelectionFilter postalAddressfilter = filter.toBuilder().birthday(birthday).gender(gender).build();
        LOGGER.debug("generatePostalAddressAsync - filter={}", postalAddressfilter);
        return postalAddressGenerator.generate(postalAddressfilter);
    }

    private PhoneNumber generatePhoneNumberAsync(SelectionFilter filter, PostalAddress postalAddress) {
        SelectionFilter phoneNumberFilter = filter.toBuilder().postalAddress(postalAddress).build();
        LOGGER.debug("generatePhoneNumberAsync - filter={}", phoneNumberFilter);
        return phoneNumberGenerator.generate(phoneNumberFilter);
    }

    private FullName generateFullNameAsync(SelectionFilter filter, LocalDate birthday, GenderIdentity gender) {
        SelectionFilter fullNameFilter = filter.toBuilder()
                .birthday(birthday)
                .gender(gender)
                .build();
        LOGGER.debug("generateFullNameAsync - filter={}", fullNameFilter);
        return fullNameGenerator.generate(fullNameFilter);
    }

    private EmailAddressRecord generateEmailAddressAsync(
            SelectionFilter filter, FullName fullName, LocalDate birthday, GenderIdentity gender, PostalAddress postalAddress
    ) {
        SelectionFilter emailAddressFilter = filter.toBuilder()
                .firstName(fullName.firstName())
                .middleName(fullName.middleName())
                .lastName(fullName.lastName())
                .birthday(birthday)
                .gender(gender)
                .postalAddress(postalAddress)
                .build();
        LOGGER.debug("generateEmailAddressAsync - filter={}", emailAddressFilter);
        return emailAddressGenerator.generate(emailAddressFilter);
    }

    @Override
    protected Class<PersonRecord> valueType() {
        return PersonRecord.class;
    }
}
