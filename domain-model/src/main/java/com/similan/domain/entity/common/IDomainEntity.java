package com.similan.domain.entity.common;

import com.similan.service.api.base.dto.key.EntityType;

public interface IDomainEntity {

  Long getId();

  EntityType getEntityType();

}
