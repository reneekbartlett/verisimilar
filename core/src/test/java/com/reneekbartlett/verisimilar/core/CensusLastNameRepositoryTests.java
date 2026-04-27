//package com.reneekbartlett.verisimilar.core;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Disabled;
////import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
////import org.junit.jupiter.api.TestInstance;
////import org.junit.jupiter.api.extension.RegisterExtension;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
////import org.hibernate.SessionFactory;
////import org.hibernate.cfg.Configuration;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
////import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
//import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import com.reneekbartlett.verisimilar.model.CensusLastName;
//import com.reneekbartlett.verisimilar.repository.CensusLastNameRepository;
//
////import jakarta.persistence.EntityManager;
////import jakarta.persistence.EntityManagerFactory;
////import jakarta.persistence.Persistence;
//
////org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class,
////org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration.class
//
//@Disabled
////@ActiveProfiles("test")
//@Testcontainers
//@DataJpaTest(
//    properties = {
//            "spring.jpa.hibernate.ddl-auto=create-drop",
//            "spring.datasource.hikari.connection-timeout=5000",
//            "spring.datasource.hikari.validation-timeout=2500",
//            "spring.datasource.hikari.minimum-idle=1"
//            //"spring.datasource.url=jdbc:tc:postgresql:18-alpine:///rb?TC_INITSCRIPT=init.sql",
//    }
//    //excludeAutoConfiguration = {
//    //org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class,
//    //org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration.class
//    //}
//)
////@DataJpaTest(properties = { "spring.flyway.enabled=true","spring.jpa.hibernate.ddl-auto=none"})
////@TestPropertySource(locations = "classpath:application-test.properties")
////@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Only needed for Spring Boot <3.1
////@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class CensusLastNameRepositoryTests {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(CensusLastNameRepositoryTests.class);
//
//    @org.testcontainers.junit.jupiter.Container
//    private static CloseablePostgresContainer postgres = new CloseablePostgresContainer("postgres:18-alpine", "init.sql");
//
//    @DynamicPropertySource
//    private static void registerProps(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.username", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
//        //registry.add("spring.datasource.hikari.connection-init-sql", 
//        //        () -> "CREATE SCHEMA IF NOT EXISTS rb; SET search_path TO rb, public;");
//        //LOGGER.info("registerProps");
//    }
//
//    @BeforeAll
//    static void start() {
//        //postgres.withInitScript("init.sql");
//        postgres.startTest();
//        LOGGER.info("started?");
//    }
//
//    @AfterAll
//    static void stop() {
//        postgres.close();
//    }
//
//    @Autowired
//    private TestEntityManager testEntityManager;
//
//    @Autowired
//    private CensusLastNameRepository censusLastNameRepository;
//
//    @Test
//    //@Disabled
//    public void CensusLastNameRepositoryTest_ReturnsExpected() {
//
//        CensusLastName censusLastName1 = new CensusLastName();
//        censusLastName1.setLastName("Smith");
//        censusLastName1.setRank(1);
//        censusLastName1.setCount(100);
//        censusLastName1.setYear(2000);
//        testEntityManager.persist(censusLastName1);
//
//        CensusLastName censusLastName2 = new CensusLastName();
//        censusLastName2.setLastName("Smartlett");
//        censusLastName1.setRank(2);
//        censusLastName1.setCount(1);
//        censusLastName1.setYear(2000);
//        testEntityManager.persist(censusLastName2);
//
//        testEntityManager.flush();
//
//        List<CensusLastName> repoResults = censusLastNameRepository.searchStartsWith("SM");
//
//        LOGGER.debug("repoResults.size()=" + repoResults.size());
//
//        // THEN: Verify the results
//        //assertThat(repoResults).hasSize(1);
//        assertThat(repoResults.size() > 0);
//        //assertThat(repoResults.get(0).getLastName()).isEqualTo("Smith");
//
////        //final SessionFactory sessionFactory = buildSessionFactory();
////        //Session session = sessionFactory.openSession();
////        //Transaction transaction = session.beginTransaction();
////
////        // 1. Create the EntityManagerFactory based on the persistence unit name in persistence.xml
////        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit")) {
////            EntityManager em = emf.createEntityManager();
////            try {
////                String jpql = "SELECT l FROM CensusLastName l WHERE l.lastName LIKE :namePattern";
////                List<CensusLastName> results = em.createQuery(jpql, CensusLastName.class)
////                        .setParameter("namePattern", "S%")
////                        .getResultList();
////            } finally {
////                em.close();
////            }
////        }
//
//        //transaction.commit();
//        //session.close();
//    }
//
////    private static SessionFactory buildSessionFactory() {
////        try {
////            // Create the SessionFactory from hibernate.cfg.xml
////            return new Configuration().configure().buildSessionFactory();
////        } catch (Throwable ex) {
////            System.err.println("Initial SessionFactory creation failed." + ex);
////            throw new ExceptionInInitializerError(ex);
////        }
////    }
//}
