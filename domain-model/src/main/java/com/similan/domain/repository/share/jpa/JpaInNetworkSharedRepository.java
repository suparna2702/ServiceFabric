package com.similan.domain.repository.share.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.share.ISharable;
import com.similan.domain.share.InNetworkShared;

public interface JpaInNetworkSharedRepository extends
    JpaRepository<InNetworkShared, Long> {

  public InNetworkShared findBySharedName(String name);

  public InNetworkShared findBySharedEntity(
      GenericReference<ISharable> sharedEntity);
}
