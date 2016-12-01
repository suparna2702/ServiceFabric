package com.similan.service.api.chat.dto.basic;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.chat.dto.key.ChatSessionKey;

public class ChatSessionDto extends KeyHolderDto<ChatSessionKey> {

  protected ChatSessionDto() {
  }

  public ChatSessionDto(ChatSessionKey key) {
    super(key);
  }

}
