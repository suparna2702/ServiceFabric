package com.similan.domain.repository.community.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.community.ExternalSocialPerson;

public interface JpaExternalSocialPersonRepository extends
    JpaRepository<ExternalSocialPerson, Long> {
  
  ExternalSocialPerson findByPrimaryEmail(String email);
  
  ExternalSocialPerson findByName(String name);

}
