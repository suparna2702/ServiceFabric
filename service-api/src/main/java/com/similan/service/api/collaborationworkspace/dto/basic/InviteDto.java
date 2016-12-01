package com.similan.service.api.collaborationworkspace.dto.basic;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.collaborationworkspace.dto.key.InviteKey;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class InviteDto extends KeyHolderDto<InviteKey> {

  @XmlElement
  private SocialActorKey inviter;

  protected InviteDto() {
  }

  public InviteDto(InviteKey key, SocialActorKey inviter) {
    super(key);
    this.inviter = inviter;
  }

  public SocialActorKey getInviter() {
    return inviter;
  }

}
