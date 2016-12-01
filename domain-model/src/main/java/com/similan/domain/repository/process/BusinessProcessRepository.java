package com.similan.domain.repository.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.process.BusinessProcess;
import com.similan.domain.repository.process.jpa.JpaBusinessProcessRepository;

@Repository
public class BusinessProcessRepository {
  @Autowired
  private JpaBusinessProcessRepository repository;

  public BusinessProcess save(BusinessProcess businessProcess) {
    return repository.save((BusinessProcess) businessProcess);
  }

  @SuppressWarnings("unchecked")
  public <PROCESS extends BusinessProcess> PROCESS findByUuid(
      Class<PROCESS> type, String uuid) {
    BusinessProcess process = repository.findByUuid(uuid);
    if (!type.isInstance(process)) {
      return null;
    }
    return (PROCESS) process;
  }
}
