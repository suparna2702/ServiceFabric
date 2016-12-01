package com.similan.domain.repository.process.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.process.BusinessProcess;

public interface JpaBusinessProcessRepository extends
    JpaRepository<BusinessProcess, String> {

  BusinessProcess findByUuid(String uuid);
}
