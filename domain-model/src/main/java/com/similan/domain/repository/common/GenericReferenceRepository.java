package com.similan.domain.repository.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.common.IDomainEntity;
import com.similan.domain.repository.common.jpa.JpaGenericReferenceRepository;
import com.similan.service.api.base.dto.key.EntityType;

@Repository
public class GenericReferenceRepository {
  @Autowired
  private JpaGenericReferenceRepository repository;
  
  public <T extends IDomainEntity> GenericReference<T> create(T entity) {
    EntityType type = entity.getEntityType();
    Long id = entity.getId();
    GenericReference<T> reference = new GenericReference<T>(type, id);
    return reference;
  }
  

}
