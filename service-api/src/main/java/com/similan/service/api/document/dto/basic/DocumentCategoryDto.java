package com.similan.service.api.document.dto.basic;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.document.dto.key.DocumentCategoryKey;

public class DocumentCategoryDto extends KeyHolderDto<DocumentCategoryKey> {

  protected DocumentCategoryDto() {
  }

  public DocumentCategoryDto(DocumentCategoryKey key) {
    super(key);
  }
}
