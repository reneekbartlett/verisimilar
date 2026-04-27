package com.reneekbartlett.verisimilar.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.reneekbartlett.verisimilar.core.model.CensusLastName;
//import com.reneekbartlett.verisimilar.service.Param;
//import com.reneekbartlett.verisimilar.service.Query;

import java.util.List;

@Repository
public interface CensusLastNameRepository extends JpaRepository<CensusLastName, Long>, CensusLastNameRepositoryCustom {

    List<CensusLastName> findByLastNameIgnoreCase(String lastName);

    @Query("SELECT c FROM CensusLastName c WHERE LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName))")
    List<CensusLastName> findByLastName(@Param("lastName") String lastName);

    // 3. Native SQL custom query
    @Query(value = "SELECT * FROM config_fullname_last_census WHERE last_name ILIKE %:lastName%", nativeQuery = true)
    List<CensusLastName> searchByLastName(@Param("lastName") String lastName);
}