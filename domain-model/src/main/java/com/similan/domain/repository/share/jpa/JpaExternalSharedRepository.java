package com.similan.domain.repository.share.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.share.ExternalShared;
import com.similan.domain.share.ISharable;

public interface JpaExternalSharedRepository extends
    JpaRepository<ExternalShared, Long> {

  public ExternalShared findBySharedName(String name);

  public ExternalShared findBySharedEntity(
      GenericReference<ISharable> sharedEntity);

}
