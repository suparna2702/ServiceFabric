package com.similan.service.api.share;

import lombok.Getter;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;

@Getter
public class InNetworkSharedKey extends Key {
  
  private String name;
  
  public InNetworkSharedKey(String name){
    this.name = name;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.INNETWORK_SHARED;
  }

}
