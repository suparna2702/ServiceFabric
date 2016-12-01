package com.similan.service.api.feed.dto.basic;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.feed.dto.key.FeedKey;

public class FeedDto extends KeyHolderDto<FeedKey> {

  protected FeedDto() {
  }

  public FeedDto(FeedKey key) {
    super(key);
  }

}
