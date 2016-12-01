package com.similan.service.impl.document;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.document.DocumentLabel;
import com.similan.domain.repository.document.DocumentLabelRepository;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.DocumentLabelService;
import com.similan.service.api.document.dto.basic.DocumentLabelDto;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentDto;
import com.similan.service.api.document.dto.extended.DocumentLabelAndDecendantsDto;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.key.DocumentLabelKey;
import com.similan.service.api.document.dto.operation.NewDocumentLabelDto;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.impl.community.SocialActorMarshaller;

@Service
public class DocumentLabelServiceImpl extends ServiceImpl implements
    DocumentLabelService {
  @Autowired
  private DocumentLabelRepository documentLabelRepository;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Autowired
  private DocumentMarshaller documentMarshaller;
  
  @Override
  @Transactional(readOnly = true)
  public List<DocumentLabelAndDecendantsDto> getRootForOwner(
      SocialActorKey ownerKey) {
    SocialActor owner = actorMarshaller
        .unmarshallActorKey(ownerKey, true);
    List<DocumentLabel> labels = documentLabelRepository
        .findByOwnerAndNullParent(owner);
    List<DocumentLabelAndDecendantsDto> labelDtos = documentMarshaller.marshallLabelsAndDecendants(labels);
    return labelDtos;
  }

  @Override
  @Transactional
  public DocumentLabelDto create(DocumentLabelKey labelKey,
      NewDocumentLabelDto newLabel) {
    SocialActorKey ownerKey = labelKey.getOwner();
    DocumentLabelKey parentKey = newLabel.getParent();
    SocialActor owner = actorMarshaller
        .unmarshallActorKey(ownerKey, true);
    String name = labelKey.getName();
    DocumentLabel parent = parentKey == null ? null : documentMarshaller.unmarshallLabelKey(parentKey, true);

    DocumentLabel label = documentLabelRepository.findOne(owner.getName(), name);
    if (label == null) {
      label = documentLabelRepository.create(owner, name, parent);
      documentLabelRepository.save(label);
    }
    
    DocumentLabelDto labelDto = documentMarshaller
        .marshallLabel(label);
    return labelDto;
  }

  @Override
  @Transactional(readOnly = true)
  public List<DocumentInstanceAndDocumentDto> getFor(DocumentLabelKey labelKey) {
    DocumentLabel label = documentMarshaller
        .unmarshallLabelKey(labelKey, true);
    List<Document> documents = label.getDocuments();
    List<DocumentInstanceAndDocumentDto> documentInstanceDtos = documentMarshaller
        .marshallDocumentInstanceAndDocuments(documents);
    return documentInstanceDtos;
  }

  @Override
  @Transactional
  public boolean add(DocumentLabelKey labelKey, DocumentKey documentKey) {
    Document document = documentMarshaller
        .unmarshallDocumentKey(documentKey, true);
    DocumentLabel label = documentMarshaller
        .unmarshallLabelKey(labelKey, true);
    java.util.Iterator<Document> iterator = label.getDocuments().iterator();
    while (iterator.hasNext()) {
      if (iterator.next().getId().equals(document.getId())) {
        return false;
      }
    }
    document.getLabels().add(label);
    label.getDocuments().add(document);
    return true;
  }

  @Override
  @Transactional
  public boolean remove(DocumentLabelKey labelKey, DocumentKey documentKey) {
    Document document = documentMarshaller
        .unmarshallDocumentKey(documentKey, true);
    DocumentLabel label = documentMarshaller
        .unmarshallLabelKey(labelKey, true);
    document.getLabels().remove(label);
    java.util.Iterator<Document> iterator = label.getDocuments().iterator();
    while (iterator.hasNext()) {
      if (iterator.next().getId().equals(document.getId())) {
        iterator.remove();
        return true;
      }
    }
    return false;
  }

}
