package com.similan.service.impl.document;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.document.DocumentCategory;
import com.similan.domain.repository.document.DocumentCategoryRepository;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.DocumentCategoryService;
import com.similan.service.api.document.dto.basic.DocumentCategoryDto;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentDto;
import com.similan.service.api.document.dto.key.DocumentCategoryKey;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.operation.NewDocumentCategoryDto;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.impl.community.SocialActorMarshaller;

@Service
public class DocumentCategoryServiceImpl extends ServiceImpl implements
    DocumentCategoryService {
  @Autowired
  private DocumentCategoryRepository documentCategoryRepository;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Autowired
  private DocumentMarshaller documentMarshaller;

  @Override
  @Transactional(readOnly = true)
  public List<DocumentCategoryDto> getForOwner(SocialActorKey ownerKey) {
    SocialActor owner = actorMarshaller
        .unmarshallActorKey(ownerKey, true);
    List<DocumentCategory> categories = documentCategoryRepository
        .findByOwner(owner);
    List<DocumentCategoryDto> categoryDtos = documentMarshaller.marshallCategories(categories);
    return categoryDtos;
  }

  @Override
  @Transactional
  public DocumentCategoryDto create(DocumentCategoryKey categoryKey,
      NewDocumentCategoryDto newCategory) {
    SocialActorKey ownerKey = categoryKey.getOwner();

    SocialActor owner = actorMarshaller
        .unmarshallActorKey(ownerKey, true);
    String name = categoryKey.getName();

    DocumentCategory category = documentCategoryRepository.findOne(owner.getName(), name);
    if (category == null) {
      category = documentCategoryRepository.create(owner, name);
      documentCategoryRepository.save(category);
    }

    DocumentCategoryDto categoryDto = documentMarshaller
        .marshallCategory(category);
    return categoryDto;
  }

  @Override
  @Transactional(readOnly = true)
  public List<DocumentInstanceAndDocumentDto> getFor(
      DocumentCategoryKey categoryKey) {
    DocumentCategory category = documentMarshaller
        .unmarshallCategoryKey(categoryKey, true);
    List<Document> documents = category.getDocuments();
    List<DocumentInstanceAndDocumentDto> documentInstanceDtos = documentMarshaller
        .marshallDocumentInstanceAndDocuments(documents);
    return documentInstanceDtos;
  }

  @Override
  @Transactional
  public boolean add(DocumentCategoryKey categoryKey, DocumentKey documentKey) {
    Document document = documentMarshaller
        .unmarshallDocumentKey(documentKey, true);
    DocumentCategory category = documentMarshaller
        .unmarshallCategoryKey(categoryKey, true);

    Iterator<Document> iterator = category.getDocuments().iterator();

    while (iterator.hasNext()) {
      if (iterator.next().getId().equals(document.getId())) {
        return false;
      }
    }
    document.getCategories().add(category);
    category.getDocuments().add(document);
    return true;
  }

  @Override
  @Transactional
  public boolean remove(DocumentCategoryKey categoryKey, DocumentKey documentKey) {
    Document document = documentMarshaller
        .unmarshallDocumentKey(documentKey, true);
    DocumentCategory category = documentMarshaller
        .unmarshallCategoryKey(categoryKey, true);
    document.getCategories().remove(category);
    Iterator<Document> iterator = category.getDocuments().iterator();
    while (iterator.hasNext()) {
      if (iterator.next().getId().equals(document.getId())) {
        iterator.remove();
        return true;
      }
    }
    return false;
  }

  @Override
  @Transactional(readOnly = true)
  public List<DocumentCategoryDto> getFor(DocumentKey documentKey) {
    Document document = documentMarshaller
        .unmarshallDocumentKey(documentKey, true);
    List<DocumentCategory> categoryList = document.getCategories();
    return documentMarshaller.marshallCategories(
        categoryList);
  }

}
