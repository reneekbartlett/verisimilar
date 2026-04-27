//package com.reneekbartlett.verisimilar.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.reneekbartlett.verisimilar.model.ProfileField;
//import com.reneekbartlett.verisimilar.io.ResourceMapLoader;
//import com.reneekbartlett.verisimilar.io.ResourceValueLoader;
//
//@Configuration
//public class GeneratorConfiguration {
//
//    // config_fullname_last_census
//    // config_fullname_first_census
//    
//    
//    @Bean
//    public ProfileField[] fullNameFileFields() {
//        return new ProfileField[] {
//                ProfileField.FIRST_NAME, ProfileField.MIDDLE_NAME, ProfileField.LAST_NAME,
//                ProfileField.NICKNAME, ProfileField.GENDER_IDENTITY 
//        };
//    }
//
//    @Bean
//    public String[] firstNameConfigFiles() {
//        return new String[]{
//                "firstnames_female_weighted_2000.txt", 
//                "firstnames_male_weighted_2000.txt"
//                };
//    }
//
//    //@Bean
//    //public String[] firstNameConfigFiles(@Value("${app.config.files.firstname}") String[] fileArray) {
//    //    return fileArray;
//    //}
//
//    @Bean
//    public String[] lastNameConfigFiles() {
//        return new String[] { "last_names_2010.txt" };
//    }
//
//    @Bean
//    public String[] postalAddressConfigFiles() {
//        //STREET_NAMES = ResourceValueLoader.loadStringList("/street_names.txt");
//        //SUFFIX_WITH_ABBR = ResourceValueLoader.loadStringList("/street_suffix.txt");
//        //CITY_STATE_ZIPS = ResourceValueLoader.loadStringList("/city_state_zip.txt");
//        //ADDRESS2_UNIT_TYPES_WEIGHTED = ResourceMapLoader.loadDoubleMap("/address_unit_types_weighted.txt");
//        return new String[] { 
//                "street_names.txt", "street_suffix.txt",
//                "address_unit_types_weighted.txt",
//                "city_state_zip.txt" 
//                };
//    }
//
//    @Bean
//    public String[] phoneNumberConfigFiles() {
//        //AREACODES_BY_STATE = ResourceMapLoader.loadArrayMap("/phone_areacode_bystate_us.txt");
//        //AL,205|251|256|334|938
//        return new String[] { "phone_areacode_bystate_us.txt" };
//    }
//
//    @Bean(name="usernameConfigFiles")
//    public String[] usernameConfigFiles() {
//        return new String[] { "username_keywords.txt" };
//    }
//
//    @Bean(name="domainConfigFiles")
//    public String[] domainConfigFiles() {
//        return new String[] { "domains_b2c_2025.txt", "domains_edu.txt", "domains_gov.txt" };
//
//    }
//
//    // TODO
//    //@Bean
//    //public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
//    //    return RedisCacheManager.builder(factory).build();
//    //}
//}
