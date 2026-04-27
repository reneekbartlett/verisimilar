//package com.reneekbartlett.verisimilar.core;
//
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
////import java.util.ArrayList;
//
//import org.apache.commons.io.IOUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.delegate.DatabaseDelegate;
//import org.testcontainers.jdbc.JdbcDatabaseDelegate;
//
//class CloseablePostgresContainer extends PostgreSQLContainer<CloseablePostgresContainer> implements AutoCloseable {
//    private static final Logger LOGGER = LoggerFactory.getLogger(CloseablePostgresContainer.class);
//
//    //private String databaseName;
//    //private final String initScriptPath;
//
//    public CloseablePostgresContainer(String imgName, String initScriptPath) {
//        super(imgName);
//        //this.initScriptPath = initScriptPath;
//        //super.withInitScript(initScriptPath);
//    }
//
//    public void startTest() {
//        //this.withInitScript(initScriptPath);
//        //this.withCreateContainerCmdModifier(cmd -> cmd.withName("postgres18"));
//
//        this.start();
//        LOGGER.info("postgres container started");
//    }
//
//    @SuppressWarnings("unused")
//    private void tryStartupScript() {
//        try {
//            URL resource = Thread.currentThread().getContextClassLoader().getResource("init.sql");
//            String scripts = IOUtils.toString(resource, StandardCharsets.UTF_8);
//        } catch (Exception e) {
//            LOGGER.error("Error", e);
//        }
//        //DatabaseDelegate databaseDelegate = postgres.getDatabaseDelegate();
//        //ScriptUtils.runInitScript(databaseDelegate, "init.sql");
//    }
//
//    @Override
//    public DatabaseDelegate getDatabaseDelegate() {
//        return new JdbcDatabaseDelegate(this, "");
//    }
//
//    @Override
//    public String getJdbcUrl() {
//        String additionalUrlParams = constructUrlParameters("?", "&");
//        return (
//            "jdbc:postgresql://" +
//            getHost() +
//            ":" +
//            getMappedPort(POSTGRESQL_PORT) +
//            "/" +
//            super.getDatabaseName() +
//            additionalUrlParams
//        );
//    }
//
//    //@Override
//    //public CloseablePostgresContainer withInitScript(String initScriptPath) {
//    //    super.withInitScript(initScriptPath);
//    //    
//    //}
//
//    @Override
//    public void close() {
//        this.stop();
//        LOGGER.info("postgres stopped");
//    }
//}
