package com.similan.service.api.share;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;

public class ExternalSharedKey extends Key {

  private String name;

  public ExternalSharedKey(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.EXTERNAL_SHARED;
  }

}
