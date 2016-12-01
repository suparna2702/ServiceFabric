package com.similan.service.api.tag;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;

public class TagKey extends Key {

  public TagKey() {

  }

  public TagKey(Long id) {
    this.setId(id);
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.TAG;
  }

}
