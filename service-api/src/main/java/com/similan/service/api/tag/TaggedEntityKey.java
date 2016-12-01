package com.similan.service.api.tag;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;

public class TaggedEntityKey extends Key {

  public TaggedEntityKey() {

  }

  public TaggedEntityKey(Long id) {
    this.setId(id);
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.TAGGED_ENTITY;
  }

}
