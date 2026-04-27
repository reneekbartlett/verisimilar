//package com.reneekbartlett.verisimilar.core;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.List;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
//import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import com.reneekbartlett.verisimilar.core.model.CensusLastName;
//import com.reneekbartlett.verisimilar.core.repository.CensusLastNameRepository;
//import com.reneekbartlett.verisimilar.core.service.CensusLastNameService;
//
//
//@ActiveProfiles("test")
//@Testcontainers
//@DataJpaTest(
//    properties = {
//            "spring.jpa.hibernate.ddl-auto=create-drop",
//    }
//)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Disabled
//public class CensusLastNameServiceTests {
//    private static final Logger LOGGER = LoggerFactory.getLogger(CensusLastNameServiceTests.class);
//
//    @Autowired
//    private CensusLastNameRepository censusLastNameRepository;
//
//    
//
//    //org.testcontainers.containers.PostgreSQLContainer
//    @org.testcontainers.junit.jupiter.Container
//    private static CloseablePostgresContainer postgres = new CloseablePostgresContainer("postgres:18-alpine", "init.sql");
//
//    private CensusLastNameService censusLastNameService;
//
//    @DynamicPropertySource
//    private static void registerProps(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.username", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
//    }
//
//    @BeforeAll
//    static void start() {
//        postgres.startTest();
//    }
//
//    @AfterAll
//    static void stop() {
//        postgres.close();
//    }
//
//    @BeforeEach
//    void setup() {
//        this.censusLastNameService = new CensusLastNameService(censusLastNameRepository);
//    }
//
//    @Test
//    //@Disabled
//    void testSaveAndFindByLastName() {
//        CensusLastName lastName = new CensusLastName();
//        lastName.setLastName("Bartlett");
//        lastName.setRank(1);
//        lastName.setCount(1);
//        lastName.setYear(2010);
//
//        censusLastNameService.save(lastName);
//
//        var results = censusLastNameService.searchByLastName("bart");
//        assertThat(results).hasSize(1);
//        assertThat(results.get(0).getLastName()).isEqualTo("Bartlett");
//    }
//
//    @Test
//    @Disabled
//    public void CensusLastNameServiceTest_ReturnsExpected() {
//        List<CensusLastName> lastNames = censusLastNameService.findAll();
//        
//        LOGGER.info("lastNames.size()=" + lastNames.size());
//        
//        assertThat(lastNames.size() > 0);
//    }
//}
