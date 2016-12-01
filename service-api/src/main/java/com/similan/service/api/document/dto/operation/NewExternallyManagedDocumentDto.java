package com.similan.service.api.document.dto.operation;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.community.dto.key.SocialActorKey;

import lombok.Getter;
import lombok.Setter;

public class NewExternallyManagedDocumentDto extends AbstractNewDocumentDto {

  @XmlAttribute
  @Getter
  @Setter
  private String externalThumbNailUrl;

  @XmlAttribute
  @Getter
  @Setter
  private String externalId;

  public NewExternallyManagedDocumentDto(SocialActorKey uploader,
      String fileName, String description, String thumbNailUrl,
      String externalId) {
    super(uploader, fileName);
    this.setDescription(description);
    this.setExternalThumbNailUrl(thumbNailUrl);
    this.setExternalId(externalId);
  }

}
