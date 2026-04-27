//package com.reneekbartlett.verisimilar.api.controller;
//
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
////import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.reneekbartlett.verisimilar.api.service.AnalysisService;
//
////import io.micrometer.core.instrument.Timer;
////import lombok.extern.slf4j.Slf4j;
//
//@RestController
//public class AnalysisController {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisController.class);
//    private final AnalysisService analysisService;
//
//    @Autowired
//    public AnalysisController(AnalysisService analysisService) {
//        this.analysisService = analysisService;
//    }
//
//    @GetMapping("/analysis")
//    public ResponseEntity<String> analysis(@RequestParam(name = "file", required = true) String file) {
//        String[] columnMap = { "0:firstName:STRING","1:middleName:STRING","2:lastName:STRING","3:birthday:LOCAL_DATE","4:addr1:STRING",
//                "5:addr2:STRING","6:city:STRING","7:state:STRING","8:zip:STRING", "9:phoneNumber:STRING","10:emailAddress:STRING" };
//        try {
//            String fileDir = "/opt/reneekbartlett/outfiles";
//            Path filePath = Paths.get(fileDir, file);
//            String out = analysisService.getAnalysis(filePath.toString(), columnMap);
//            return ResponseEntity.ok(out);
//        } catch (Exception e) {
//            LOGGER.error("Error.", e);
//            return ResponseEntity.internalServerError().body("");
//        }
//    }
//}
