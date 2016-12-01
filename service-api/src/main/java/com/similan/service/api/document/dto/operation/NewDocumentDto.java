package com.similan.service.api.document.dto.operation;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import lombok.Getter;

import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.key.DocumentCategoryKey;
import com.similan.service.api.document.dto.key.DocumentLabelKey;

public class NewDocumentDto extends AbstractNewDocumentDto {

  @XmlElement(name = "label")
  @XmlElementWrapper
  @Getter
  private List<DocumentLabelKey> labels;

  @XmlElement(name = "category")
  @XmlElementWrapper
  @Getter
  private List<DocumentCategoryKey> categories;

  public NewDocumentDto() {
  }

  public NewDocumentDto(String uploaderName, String description,
      List<DocumentLabelKey> labels, List<DocumentCategoryKey> categories,
      String explicitFilename) {
    this(new SocialActorKey(uploaderName), description, labels, categories,
        explicitFilename);
  }

  public NewDocumentDto(SocialActorKey uploader, String description,
      List<DocumentLabelKey> labels, List<DocumentCategoryKey> categories,
      String explicitFilename) {
    super(uploader, explicitFilename);
    this.setDescription(description);
    this.labels = labels;
    this.categories = categories;
  }

}
