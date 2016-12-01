package com.similan.domain.repository.account.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.acccount.BusinessAccountType;

public interface JpaBusinessAccountTypeRepository extends
    JpaRepository<BusinessAccountType, Long> {
  
  BusinessAccountType findByName(String name);
}
