package com.similan.service.api.advertisement.dto.key;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;
import com.similan.service.api.comment.dto.key.ICommentableKey;
import com.similan.service.api.wall.dto.key.IWallEntrySubjectKey;

public class DisplayNoticeKey extends Key implements ICommentableKey,
    IWallEntrySubjectKey {

  public DisplayNoticeKey() {

  }

  public DisplayNoticeKey(String id) {
    Long keyId = Long.valueOf(id);
    this.setId(keyId);
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.DISPLAY_NOTICE;
  }

}
