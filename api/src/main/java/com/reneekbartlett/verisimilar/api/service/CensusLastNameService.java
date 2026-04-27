//package com.reneekbartlett.verisimilar.service;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.springframework.stereotype.Service;
//
//import com.reneekbartlett.verisimilar.core.model.CensusLastName;
//import com.reneekbartlett.verisimilar.repository.CensusLastNameRepository;
//
//import java.util.List;
//
//@Service
//public class CensusLastNameService {
//    //@PersistenceContext
//    //private EntityManager entityManager;
//
//    private final CensusLastNameRepository lastNameRepository;
//
//    public CensusLastNameService(CensusLastNameRepository lastNameRepository) {
//        this.lastNameRepository = lastNameRepository;
//    }
//
//    public List<CensusLastName> findAll() {
//        return lastNameRepository.findAll();
//    }
//
//    public CensusLastName save(CensusLastName c) {
//        return lastNameRepository.save(c);
//    }
//
//    public List<CensusLastName> searchByLastName(String lastName){
//        return lastNameRepository.searchByLastName(lastName);
//    }
//
//    public List<CensusLastName> searchByLastName(CensusLastName c){
//        return lastNameRepository.searchByLastName(c.getLastName());
//    }
//
//    //public List<CensusLastName> customSelectQuery(String lastName) {
//    //    return entityManager.createQuery("SELECT u FROM CensusLastName u WHERE u.lastName = :lastName", CensusLastName.class)
//    //            .setParameter("lastName", lastName)
//    //            .getResultList();
//    //}
//}
