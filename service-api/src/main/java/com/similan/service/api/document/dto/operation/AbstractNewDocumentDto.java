package com.similan.service.api.document.dto.operation;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

import com.similan.service.api.base.dto.operation.OperationDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

public abstract class AbstractNewDocumentDto extends OperationDto {

  @XmlElement
  @Getter
  private SocialActorKey uploader;

  @XmlAttribute
  @Getter
  private String explicitFilename;

  @XmlAttribute
  @Getter
  @Setter
  private String description;

  protected AbstractNewDocumentDto() {
  }

  public AbstractNewDocumentDto(SocialActorKey uploader, String explicitFilename) {
    this.uploader = uploader;
    this.explicitFilename = explicitFilename;
  }

}
