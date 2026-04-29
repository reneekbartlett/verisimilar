package com.reneekbartlett.verisimilar.core.datasets.resolver.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.reneekbartlett.verisimilar.core.datasets.resolver.AddressTwoDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.AreaCodeDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.CityStateZipDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.DatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.DomainDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.FirstNameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.KeywordDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.LastNameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.MiddleNameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.NicknameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.StreetNameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.StreetSuffixDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.UsernameDatasetResolver;

/***
 * 
 */
public class DatasetResolverRegistry {
    private final FirstNameDatasetResolver firstNameResolver;
    private final MiddleNameDatasetResolver middleNameResolver;
    private final LastNameDatasetResolver lastNameResolver;
    private final NicknameDatasetResolver nicknameResolver;

    private final StreetNameDatasetResolver streetNameResolver;
    private final StreetSuffixDatasetResolver streetSuffixResolver;
    private final AddressTwoDatasetResolver addressTwoResolver;
    private final CityStateZipDatasetResolver cityStateZipResolver;

    private final UsernameDatasetResolver usernameResolver;
    private final DomainDatasetResolver domainResolver;

    private final AreaCodeDatasetResolver areaCodeResolver;
    
    private final KeywordDatasetResolver keywordResolver;

    private final Map<Class<?>, DatasetResolver<?, ?>> resolvers = new HashMap<>();

    /***
     * 
     * @param firstNameResolver
     * @param middleNameResolver
     * @param lastNameResolver
     * @param nicknameResolver
     * @param streetNameResolver
     * @param streetSuffixResolver
     * @param addressTwoResolver
     * @param cityStateZipResolver
     * @param areaCodeResolver
     * @param usernameResolver
     * @param domainResolver
     * @param keywordResolver
     */
    public DatasetResolverRegistry(
            FirstNameDatasetResolver firstNameResolver,
            MiddleNameDatasetResolver middleNameResolver,
            LastNameDatasetResolver lastNameResolver,
            NicknameDatasetResolver nicknameResolver,

            StreetNameDatasetResolver streetNameResolver,
            StreetSuffixDatasetResolver streetSuffixResolver,
            AddressTwoDatasetResolver addressTwoResolver,
            CityStateZipDatasetResolver cityStateZipResolver,

            AreaCodeDatasetResolver areaCodeResolver,

            UsernameDatasetResolver usernameResolver,
            DomainDatasetResolver domainResolver,
            KeywordDatasetResolver keywordResolver
    ) {
        this.firstNameResolver = firstNameResolver;
        this.middleNameResolver = middleNameResolver;
        this.lastNameResolver = lastNameResolver;
        this.nicknameResolver = nicknameResolver;

        this.streetNameResolver = streetNameResolver;
        this.streetSuffixResolver = streetSuffixResolver;

        this.addressTwoResolver = addressTwoResolver;
        this.cityStateZipResolver = cityStateZipResolver;

        this.areaCodeResolver = areaCodeResolver;

        this.usernameResolver = usernameResolver;
        this.domainResolver = domainResolver;

        this.keywordResolver = keywordResolver;

        this.registerAll();
    }

    /**
     * Register a resolver for its key type.
     */
    public <K, R> void register(DatasetResolver<K, R> resolver) {
        Objects.requireNonNull(resolver, "resolver");
        Objects.requireNonNull(resolver.keyType(), "keyType");
        resolvers.put(resolver.keyType(), resolver);
    }

    public final <K, R> void registerAll(Collection<DatasetResolver<?, ?>> resolvers) {
        resolvers.forEach(resolver -> {
            if(resolver != null) {
                register(resolver);
            }
        });
    }

    public final void registerAll() {
        Collection<DatasetResolver<?, ?>> resolvers = new ArrayList<>();
        if(this.firstNameResolver != null) resolvers.add(firstNameResolver);
        if(this.middleNameResolver != null) resolvers.add(middleNameResolver);
        if(this.lastNameResolver != null) resolvers.add(lastNameResolver);
        if(this.streetNameResolver != null) resolvers.add(streetNameResolver);
        if(this.streetSuffixResolver != null) resolvers.add(streetSuffixResolver);
        if(this.addressTwoResolver != null) resolvers.add(addressTwoResolver);
        if(this.cityStateZipResolver != null) resolvers.add(cityStateZipResolver);
        if(this.areaCodeResolver != null) resolvers.add(areaCodeResolver);
        if(this.nicknameResolver != null) resolvers.add(nicknameResolver);
        if(this.usernameResolver != null) resolvers.add(usernameResolver);
        if(this.domainResolver != null) resolvers.add(domainResolver);
        if(this.keywordResolver != null) resolvers.add(keywordResolver);
        this.registerAll(resolvers);
        return;
    }

    // TODO: Check logic
    @SuppressWarnings("unchecked")
    public <K, R> DatasetResolver<K,R> getResolver(Class<K> keyType) {
        DatasetResolver<?, ?> resolver = resolvers.get(keyType);
        if (resolver == null) {
            throw new IllegalStateException("No resolver registered for key type: " + keyType.getName());
        }
        return (DatasetResolver<K, R>) resolver;
    }

    public FirstNameDatasetResolver first() {
        return firstNameResolver;
    }

    public MiddleNameDatasetResolver middle() {
        return middleNameResolver;
    }

    public LastNameDatasetResolver last() {
        return lastNameResolver;
    }

    public StreetNameDatasetResolver streetName() {
        return streetNameResolver;
    }

    public StreetSuffixDatasetResolver streetSuffix() {
        return streetSuffixResolver;
    }

    public AddressTwoDatasetResolver address2() {
        return addressTwoResolver;
    }

    public CityStateZipDatasetResolver cityStateZip() {
        return cityStateZipResolver;
    }

    public UsernameDatasetResolver username() {
        return usernameResolver;
    }

    public DomainDatasetResolver domain() {
        return domainResolver;
    }

    public NicknameDatasetResolver nickname() {
        return nicknameResolver;
    }

    public AreaCodeDatasetResolver areaCode() {
        return areaCodeResolver;
    }
    
    public KeywordDatasetResolver keyword() {
        return keywordResolver;
    }
}
