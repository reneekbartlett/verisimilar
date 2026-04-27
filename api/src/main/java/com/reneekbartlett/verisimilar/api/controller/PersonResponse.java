//package com.reneekbartlett.verisimilar.api.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import com.reneekbartlett.verisimilar.api.dto.PersonRequestDto;
//import com.reneekbartlett.verisimilar.api.dto.PersonResponseDto;
//import com.reneekbartlett.verisimilar.api.service.PersonService;
////import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
////import com.reneekbartlett.verisimilar.core.model.USState;
////import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
//
//public class PersonResponse {
//
//    private final PersonService personService;
//
//    @Autowired
//    public PersonResponse(PersonService personService) {
//        this.personService = personService;
//    }
//
//    @GetMapping("/person")
//    public PersonResponseDto generate(PersonRequestDto req) {
//        //GenderIdentity gender = GenderIdentity.valueOf(req.gender().toUpperCase());
//        //USState state = req.state() != null ? USState.valueOf(req.state().toUpperCase()) : null;
//        /*DatasetResolutionContext ctx = new DatasetResolutionContext(
//                req.year(),
//                req.gender() != null ? GenderIdentity.valueOf(req.gender().toUpperCase()) : null,
//                req.state() != null ? USState.valueOf(req.state().toUpperCase()) : null,
//                req.ethnicity() != null ? Ethnicity.valueOf(req.ethnicity().toUpperCase()) : null,
//                req.region() != null ? USRegion.valueOf(req.region().toUpperCase()) : null
//            );
//        */
//
//        return personService.generate();
//    }
//}
