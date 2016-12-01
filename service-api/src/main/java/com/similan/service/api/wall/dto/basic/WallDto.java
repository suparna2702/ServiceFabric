package com.similan.service.api.wall.dto.basic;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.wall.dto.key.IWallContainerKey;
import com.similan.service.api.wall.dto.key.WallKey;

public class WallDto<WallContainerKey extends IWallContainerKey> extends
    KeyHolderDto<WallKey<WallContainerKey>> {

  protected WallDto() {
  }

  public WallDto(WallKey<WallContainerKey> key) {
    super(key);
  }

  @Override
  public String toString() {
	return "WallDto [getKey()=" + getKey() + "]";
  }

}
