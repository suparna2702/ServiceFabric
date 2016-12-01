package com.similan.service.api.document;

import java.util.List;

import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.basic.DocumentLabelDto;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentDto;
import com.similan.service.api.document.dto.extended.DocumentLabelAndDecendantsDto;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.key.DocumentLabelKey;
import com.similan.service.api.document.dto.operation.NewDocumentLabelDto;

public interface DocumentLabelService {

  DocumentLabelDto create(DocumentLabelKey labelKey,
      NewDocumentLabelDto newLabel);

  List<DocumentLabelAndDecendantsDto> getRootForOwner(SocialActorKey ownerKey);

  boolean add(DocumentLabelKey labelKey, DocumentKey documentKey);

  boolean remove(DocumentLabelKey labelKey, DocumentKey documentKey);

  List<DocumentInstanceAndDocumentDto> getFor(DocumentLabelKey labelKey);
}