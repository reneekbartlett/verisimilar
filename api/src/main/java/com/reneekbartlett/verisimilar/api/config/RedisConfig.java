//package com.reneekbartlett.verisimilar.config;
//
//import java.time.Duration;
//
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
//import org.springframework.data.redis.serializer.RedisSerializer;
//
//// TODO
//@Profile("!test")
//@Configuration
////@EnableCaching
//public class RedisConfig {
//    
////    @Bean
////    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
////        RedisTemplate<String, Object> template = new RedisTemplate<>();
////        template.setConnectionFactory(connectionFactory);
////        
////        // New Spring Data Redis 4.x compatible serializer
////        GenericJacksonJsonRedisSerializer serializer = new GenericJacksonJsonRedisSerializer();
////        
////        template.setValueSerializer(serializer);
////        template.setHashValueSerializer(serializer);
////        template.afterPropertiesSet();
////        return template;
////    }
//
//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
//        GenericJacksonJsonRedisSerializer serializer = GenericJacksonJsonRedisSerializer.builder()
//                .build();
//        RedisCacheConfiguration defaultConfig =
//                RedisCacheConfiguration.defaultCacheConfig()
//                        .entryTtl(Duration.ofHours(1))
//                        .disableCachingNullValues()
//                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
//        return RedisCacheManager.builder(factory)
//                .cacheDefaults(defaultConfig)
//                .build();
//    }
//}
