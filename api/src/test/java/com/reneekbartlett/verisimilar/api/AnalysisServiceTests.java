//package com.reneekbartlett.verisimilar.api;
//
//import java.time.LocalDate;
//
////import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.Assert;
//
//public class AnalysisServiceTests {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisServiceTests.class);
//
//    @Test
//    @org.junit.jupiter.api.Disabled
//    public void testFakeFileAnalysis() throws Exception {
//        // TODO
//        AnalysisService analysisService = new AnalysisService();
//
//        String file = "/opt/reneekbartlett/outfiles/people_10000_20260903_113936888_0.csv";
//        String[] columnMap = {"0:firstName:STRING","1:middleName:STRING","2:lastName:STRING","3:birthday:LOCAL_DATE","4:addr1:STRING",
//                "5:addr2:STRING","6:city:STRING","7:state:STRING","8:zip:STRING", "9:phoneNumber:STRING","10:emailAddress:STRING" };
//        String result = analysisService.getAnalysis(file, columnMap);
//        LOGGER.info(result);
//    }
//}
