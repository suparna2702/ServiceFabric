package com.similan.service.api.document;

import java.util.List;

import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.basic.DocumentCategoryDto;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentDto;
import com.similan.service.api.document.dto.key.DocumentCategoryKey;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.operation.NewDocumentCategoryDto;

public interface DocumentCategoryService {

  DocumentCategoryDto create(DocumentCategoryKey categoryKey,
      NewDocumentCategoryDto newCategory);

  List<DocumentCategoryDto> getForOwner(SocialActorKey ownerKey);

  boolean add(DocumentCategoryKey categoryKey, DocumentKey documentKey);

  boolean remove(DocumentCategoryKey categoryKey, DocumentKey documentKey);

  List<DocumentInstanceAndDocumentDto> getFor(DocumentCategoryKey categoryKey);
  
  List<DocumentCategoryDto> getFor(DocumentKey documentKey);
}