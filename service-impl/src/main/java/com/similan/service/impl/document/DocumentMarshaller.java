package com.similan.service.impl.document;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.document.DocumentCategory;
import com.similan.domain.entity.document.DocumentInstance;
import com.similan.domain.entity.document.DocumentLabel;
import com.similan.domain.entity.managementworkspace.ManagementWorkspace;
import com.similan.domain.repository.document.DocumentCategoryRepository;
import com.similan.domain.repository.document.DocumentLabelRepository;
import com.similan.domain.repository.document.DocumentRepository;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.basic.DocumentCategoryDto;
import com.similan.service.api.document.dto.basic.DocumentDto;
import com.similan.service.api.document.dto.basic.DocumentLabelDto;
import com.similan.service.api.document.dto.error.DocumentErrorCode;
import com.similan.service.api.document.dto.error.DocumentException;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentDto;
import com.similan.service.api.document.dto.extended.DocumentLabelAndDecendantsDto;
import com.similan.service.api.document.dto.key.DocumentCategoryKey;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.key.DocumentLabelKey;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.impl.Marshaller;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.impl.managementworkspace.ManagementWorkspaceMarshaller;

@Component
public class DocumentMarshaller extends Marshaller {
  @Autowired
  DocumentRepository documentRepository;
  @Autowired
  private DocumentLabelRepository documentLabelRepository;
  @Autowired
  private DocumentCategoryRepository documentCategoryRepository;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Autowired
  private ManagementWorkspaceMarshaller managementWorkspaceMarshaller;
  @Autowired
  private DocumentInstanceMarshaller documentInstanceMarshaller;

  public Document unmarshallDocumentKey(DocumentKey documentKey,
      boolean required) {
    ManagementWorkspaceKey workspaceKey = documentKey.getWorkspace();
    SocialActorKey ownerKey = workspaceKey.getOwner();

    String workspaceOwnerName = ownerKey.getName();
    String workspaceName = workspaceKey.getName();
    String name = documentKey.getName();

    Document document = documentRepository.findOne(workspaceOwnerName,
        workspaceName, name);
    if (document == null && required) {
      throw new DocumentException(DocumentErrorCode.DOCUMENT_NOT_FOUND,
          "Document " + documentKey + " not found.");
    }
    return document;
  }

  public DocumentKey marshallDocumentKey(Document document) {
    ManagementWorkspace workspace = document.getWorkspace();

    ManagementWorkspaceKey workspaceKey = managementWorkspaceMarshaller
        .marshallWorkspaceKey(workspace);
    String name = document.getName();

    DocumentKey key = new DocumentKey(workspaceKey, name);
    key.setId(document.getId());

    return key;
  }

  public DocumentDto marshallDocument(Document document) {
    List<DocumentLabel> labels = document.getLabels();
    List<DocumentCategory> categories = document.getCategories();

    DocumentKey key = marshallDocumentKey(document);
    String description = document.getDescription();
    List<DocumentLabelKey> labelKeys = marshallLabelKeys(labels);
    List<DocumentCategoryKey> categoryKeys = marshallCategoryKeys(categories);

    DocumentDto documentDto = new DocumentDto(key, description, labelKeys,
        categoryKeys);
    return documentDto;
  }

  public List<DocumentCategoryKey> marshallCategoryKeys(
      List<DocumentCategory> categories) {
    List<DocumentCategoryKey> categoryKeys = new ArrayList<DocumentCategoryKey>(
        categories.size());
    for (DocumentCategory category : categories) {
      DocumentCategoryKey categoryKey = marshallCategoryKey(category);
      categoryKeys.add(categoryKey);
    }
    return categoryKeys;
  }

  public DocumentCategoryKey marshallCategoryKey(DocumentCategory category) {
    SocialActor owner = category.getOwner();

    SocialActorKey ownerKey = actorMarshaller.marshallActorKey(owner);
    String name = category.getName();

    DocumentCategoryKey key = new DocumentCategoryKey(ownerKey, name);
    key.setId(category.getId());

    return key;
  }

  public List<DocumentLabelKey> marshallLabelKeys(List<DocumentLabel> labels) {
    List<DocumentLabelKey> labelKeys = new ArrayList<DocumentLabelKey>(
        labels.size());
    for (DocumentLabel label : labels) {
      DocumentLabelKey labelKey = marshallLabelKey(label);
      labelKeys.add(labelKey);
    }
    return labelKeys;
  }

  public DocumentLabelKey marshallLabelKey(DocumentLabel label) {
    SocialActor owner = label.getOwner();

    SocialActorKey ownerKey = actorMarshaller.marshallActorKey(owner);
    String name = label.getName();

    DocumentLabelKey key = new DocumentLabelKey(ownerKey, name);
    key.setId(label.getId());

    return key;
  }

  public List<DocumentLabel> unmarshallLabelKeys(
      List<DocumentLabelKey> labelKeys) {
    List<DocumentLabel> labels = new ArrayList<DocumentLabel>(labelKeys.size());
    for (DocumentLabelKey labelKey : labelKeys) {
      DocumentLabel label = unmarshallLabelKey(labelKey, true);
      labels.add(label);
    }
    return labels;
  }

  public DocumentLabel unmarshallLabelKey(DocumentLabelKey labelKey,
      boolean required) {
    SocialActorKey ownerKey = labelKey.getOwner();

    String ownerName = ownerKey.getName();
    String name = labelKey.getName();

    DocumentLabel label = documentLabelRepository.findOne(ownerName, name);
    if (label == null && required) {
      throw new DocumentException(DocumentErrorCode.DOCUMENT_LABEL_NOT_FOUND,
          "Document label " + labelKey + " not found.");
    }
    return label;
  }

  public List<DocumentCategory> unmarshallCategoryKeys(
      List<DocumentCategoryKey> categoryKeys) {
    List<DocumentCategory> categorys = new ArrayList<DocumentCategory>(
        categoryKeys.size());
    for (DocumentCategoryKey categoryKey : categoryKeys) {
      DocumentCategory category = unmarshallCategoryKey(categoryKey, true);
      categorys.add(category);
    }
    return categorys;
  }

  public DocumentCategory unmarshallCategoryKey(
      DocumentCategoryKey categoryKey, boolean required) {
    SocialActorKey ownerKey = categoryKey.getOwner();

    String ownerName = ownerKey.getName();
    String name = categoryKey.getName();

    DocumentCategory category = documentCategoryRepository.findOne(ownerName,
        name);
    if (category == null && required) {
      throw new DocumentException(
          DocumentErrorCode.DOCUMENT_CATEGORY_NOT_FOUND, "Document category "
              + categoryKey + " not found.");
    }
    return category;
  }

  public List<DocumentCategoryDto> marshallCategories(
      List<DocumentCategory> categories) {
    List<DocumentCategoryDto> categoryDtos = new ArrayList<DocumentCategoryDto>(
        categories.size());
    for (DocumentCategory category : categories) {
      DocumentCategoryDto categoryDto = marshallCategory(category);
      categoryDtos.add(categoryDto);
    }
    return categoryDtos;
  }

  public DocumentCategoryDto marshallCategory(DocumentCategory category) {
    SocialActor owner = category.getOwner();

    SocialActorKey ownerKey = actorMarshaller.marshallActorKey(owner);
    String name = category.getName();

    DocumentCategoryKey key = new DocumentCategoryKey(ownerKey, name);
    DocumentCategoryDto categoryDto = new DocumentCategoryDto(key);
    return categoryDto;
  }

  public List<DocumentLabelDto> marshallLabels(List<DocumentLabel> labels) {
    return marshallChildLabels(null, labels);
  }

  private List<DocumentLabelDto> marshallChildLabels(
      DocumentLabelKey parentKey, List<DocumentLabel> labels) {
    List<DocumentLabelDto> labelDtos = new ArrayList<DocumentLabelDto>(
        labels.size());
    for (DocumentLabel label : labels) {
      DocumentLabelDto labelDto = marshallLabel(parentKey, label);
      labelDtos.add(labelDto);
    }
    return labelDtos;
  }

  public DocumentLabelDto marshallLabel(DocumentLabel label) {
    return marshallLabel(null, label);
  }

  private DocumentLabelDto marshallLabel(DocumentLabelKey parentKey,
      DocumentLabel label) {
    SocialActor owner = label.getOwner();

    SocialActorKey ownerKey = actorMarshaller.marshallActorKey(owner);
    String name = label.getName();
    if (parentKey == null) {
      DocumentLabel parent = label.getParent();
      parentKey = parent == null ? null : marshallLabelKey(parent);
    }

    DocumentLabelKey key = new DocumentLabelKey(ownerKey, name);
    DocumentLabelDto labelDto = new DocumentLabelDto(key, parentKey);
    return labelDto;
  }

  public List<DocumentLabelAndDecendantsDto> marshallLabelsAndDecendants(
      List<DocumentLabel> labels) {
    return marshallLabelsAndDecendants(null, labels);
  }

  private List<DocumentLabelAndDecendantsDto> marshallLabelsAndDecendants(
      DocumentLabelKey parentKey, List<DocumentLabel> labels) {
    List<DocumentLabelAndDecendantsDto> labelAndDecendantsDtos = new ArrayList<DocumentLabelAndDecendantsDto>(
        labels.size());
    for (DocumentLabel label : labels) {
      DocumentLabelAndDecendantsDto labelAndDecendantsDto = marshallLabelAndDecendants(
          parentKey, label);
      labelAndDecendantsDtos.add(labelAndDecendantsDto);
    }
    return labelAndDecendantsDtos;
  }

  public DocumentLabelAndDecendantsDto marshallLabelAndDecendants(
      DocumentLabel label) {
    return marshallLabelAndDecendants(null, label);
  }

  private DocumentLabelAndDecendantsDto marshallLabelAndDecendants(
      DocumentLabelKey parentKey, DocumentLabel label) {
    SocialActor owner = label.getOwner();
    List<DocumentLabel> children = label.getChildren();
    SocialActorKey ownerKey = actorMarshaller.marshallActorKey(owner);
    String name = label.getName();
    if (parentKey == null) {
      DocumentLabel parent = label.getParent();
      parentKey = parent == null ? null : marshallLabelKey(parent);
    }
    DocumentLabelKey key = new DocumentLabelKey(ownerKey, name);
    List<DocumentLabelAndDecendantsDto> childrenDtos = marshallLabelsAndDecendants(
        key, children);

    DocumentLabelAndDecendantsDto labelAndDecendantsDto = new DocumentLabelAndDecendantsDto(
        key, parentKey, childrenDtos);
    return labelAndDecendantsDto;
  }

  public List<DocumentInstanceAndDocumentDto> marshallDocumentInstanceAndDocuments(
      List<Document> documents) {
    List<DocumentInstanceAndDocumentDto> documentInstanceAndDocumentDtos = new ArrayList<DocumentInstanceAndDocumentDto>(
        documents.size());
    for (Document document : documents) {
      DocumentInstanceAndDocumentDto documentInstanceAndDocumentDto = marshallDocumentInstanceAndDocument(document);
      documentInstanceAndDocumentDtos.add(documentInstanceAndDocumentDto);
    }
    return documentInstanceAndDocumentDtos;
  }

  public DocumentInstanceAndDocumentDto marshallDocumentInstanceAndDocument(
      Document document) {
    DocumentInstance lastInstance = document.getLastInstance();
    return documentInstanceMarshaller
        .marshallDocumentInstanceAndDocument(lastInstance);
  }
}
