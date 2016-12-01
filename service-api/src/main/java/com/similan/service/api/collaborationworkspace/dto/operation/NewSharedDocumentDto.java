package com.similan.service.api.collaborationworkspace.dto.operation;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.operation.OperationDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class NewSharedDocumentDto extends OperationDto {

  @XmlElement
  private SocialActorKey sharer;

  public NewSharedDocumentDto() {
  }

  public NewSharedDocumentDto(SocialActorKey sharer) {
    this.sharer = sharer;
  }

  public SocialActorKey getSharer() {
    return sharer;
  }
}