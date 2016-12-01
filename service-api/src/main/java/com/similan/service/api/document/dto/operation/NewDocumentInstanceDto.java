package com.similan.service.api.document.dto.operation;

import com.similan.service.api.community.dto.key.SocialActorKey;

public class NewDocumentInstanceDto extends AbstractNewDocumentDto {

  public NewDocumentInstanceDto() {
  }

  public NewDocumentInstanceDto(String uploaderName, String explicitFilename) {
    this(new SocialActorKey(uploaderName), explicitFilename);
  }

  public NewDocumentInstanceDto(SocialActorKey uploader, String explicitFilename) {
    super(uploader, explicitFilename);
  }

}
