package com.reneekbartlett.verisimilar.api.repository;

import java.util.List;

import com.reneekbartlett.verisimilar.api.model.CensusLastName;

public interface CensusLastNameRepositoryCustom {
  List<CensusLastName> searchFlexible(String text);

  List<CensusLastName> searchStartsWith(String text);
}
