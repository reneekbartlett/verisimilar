package com.reneekbartlett.verisimilar.api.repository;

import java.util.List;
//import java.util.Optional;
//import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.CensusLastName;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CensusLastNameRepositoryCustomImpl implements CensusLastNameRepositoryCustom {

    private static final Logger LOGGER = LoggerFactory.getLogger(CensusLastNameRepositoryCustomImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CensusLastName> searchStartsWith(String text) {
        String jpql = "SELECT c FROM CensusLastName c WHERE UPPER(c.lastName) LIKE UPPER(CONCAT('', :text, '%'))";
        //OR CAST(c.year as varchar) LIKE CONCAT('%', :text, '%')

        LOGGER.info(jpql);

        return entityManager.createQuery(jpql, CensusLastName.class)
                .setParameter("text", text)
                .getResultList();
    }

    @Override
    public List<CensusLastName> searchFlexible(String text) {
        String jpql = """
                    SELECT c FROM CensusLastName c
                    WHERE LOWER(c.lastName) LIKE UPPER(CONCAT('%', :text, '%'))
                """;
        //OR CAST(c.year as varchar) LIKE CONCAT('%', :text, '%')

        LOGGER.info(jpql);

        return entityManager.createQuery(jpql, CensusLastName.class)
                .setParameter("text", text)
                .getResultList();
    }

}
