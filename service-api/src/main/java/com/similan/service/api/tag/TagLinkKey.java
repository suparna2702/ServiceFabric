package com.similan.service.api.tag;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;

public class TagLinkKey extends Key {

  public TagLinkKey() {

  }

  public TagLinkKey(Long id) {
    this.setId(id);
  }

  @Override
  public EntityType getEntityType() {
    return null;
  }

}
