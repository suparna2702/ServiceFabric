package com.similan.service.impl.common;

import javax.management.openmbean.InvalidKeyException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.advertisement.DisplayNotice;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceParticipation;
import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.collaborationworkspace.Task;
import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.comment.CommentReply;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.common.IDomainEntity;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.document.DocumentCategory;
import com.similan.domain.entity.document.DocumentInstance;
import com.similan.domain.entity.document.DocumentLabel;
import com.similan.domain.entity.document.DocumentPage;
import com.similan.domain.entity.document.DocumentViewerItem;
import com.similan.domain.entity.managementworkspace.ManagementWorkspace;
import com.similan.domain.repository.advertisement.DisplayNoticeRepository;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceParticipationRepository;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceRepository;
import com.similan.domain.repository.collaborationworkspace.SharedDocumentRepository;
import com.similan.domain.repository.collaborationworkspace.TaskRepository;
import com.similan.domain.repository.comment.CommentReplyRepository;
import com.similan.domain.repository.comment.CommentRepository;
import com.similan.domain.repository.community.SocialActorRepository;
import com.similan.domain.repository.document.DocumentCategoryRepository;
import com.similan.domain.repository.document.DocumentInstanceRepository;
import com.similan.domain.repository.document.DocumentLabelRepository;
import com.similan.domain.repository.document.DocumentPageRepository;
import com.similan.domain.repository.document.DocumentRepository;
import com.similan.domain.repository.document.DocumentViewerItemRepository;
import com.similan.domain.repository.managementworkspace.ManagementWorkspaceRepository;
import com.similan.service.api.advertisement.dto.key.DisplayNoticeKey;
import com.similan.service.api.base.dto.basic.IKeyHolderDto;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.IKey;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceParticipationKey;
import com.similan.service.api.collaborationworkspace.dto.key.SharedDocumentKey;
import com.similan.service.api.collaborationworkspace.dto.key.TaskKey;
import com.similan.service.api.comment.dto.key.CommentKey;
import com.similan.service.api.comment.dto.key.CommentReplyKey;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.key.DocumentCategoryKey;
import com.similan.service.api.document.dto.key.DocumentInstanceKey;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.key.DocumentLabelKey;
import com.similan.service.api.document.dto.key.DocumentPageKey;
import com.similan.service.api.document.dto.key.DocumentViewerItemKey;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.impl.Marshaller;
import com.similan.service.impl.advertisement.DisplayNoticeMarshaller;
import com.similan.service.impl.collaborationworkspace.CollaborationWorkspaceMarshaller;
import com.similan.service.impl.comment.CommentMarshaller;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.impl.document.DocumentInstanceMarshaller;
import com.similan.service.impl.document.DocumentMarshaller;
import com.similan.service.impl.managementworkspace.ManagementWorkspaceMarshaller;

@Component
public class GenericReferenceMarshaller extends Marshaller {
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private CommentReplyRepository commentReplyRepository;
  @Autowired
  private DocumentRepository documentRepository;
  @Autowired
  private DocumentCategoryRepository documentCategoryRepository;
  @Autowired
  private DocumentInstanceRepository documentInstanceRepository;
  @Autowired
  private DocumentLabelRepository documentLabelRepository;
  @Autowired
  private DocumentPageRepository documentPageRepository;
  @Autowired
  private DocumentViewerItemRepository documentViewerItemRepository;
  @Autowired
  private ManagementWorkspaceRepository managementWorkspaceRepository;
  @Autowired
  private CollaborationWorkspaceRepository collaborationWorkspaceRepository;
  @Autowired
  private CollaborationWorkspaceParticipationRepository collaborationWorkspaceParticipationRepository;
  @Autowired
  private SocialActorRepository socialActorRepository;
  @Autowired
  private SharedDocumentRepository sharedDocumentRepository;
  @Autowired
  private TaskRepository taskRepository;
  @Autowired
  private DisplayNoticeRepository displayNoticeRepository;
  @Autowired
  private CollaborationWorkspaceMarshaller collaborationWorkspaceMarshaller;
  @Autowired
  private CommentMarshaller commentMarshaller;
  @Autowired
  private DocumentMarshaller documentMarshaller;
  @Autowired
  private DocumentInstanceMarshaller documentInstanceMarshaller;
  @Autowired
  private ManagementWorkspaceMarshaller managementWorkspaceMarshaller;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Autowired
  private DisplayNoticeMarshaller displayNoticeMarshaller;

  public IDomainEntity unmarshallKey(IKey key, boolean required) {
    EntityType type = key.getEntityType();
    switch (type) {
    case COLLABORATION_WORKSPACE:
      return collaborationWorkspaceMarshaller
          .unmarshallWorkspaceKey((CollaborationWorkspaceKey) key, required);
    case COLLABORATION_WORKSPACE_PARTICIPATION:
      return collaborationWorkspaceMarshaller
          .unmarshallParticipationKey(
              (CollaborationWorkspaceParticipationKey) key, required);
    case COMMENT:
      return commentMarshaller.unmarshallCommentKey(
          (CommentKey<?>) key, required);
    case COMMENT_REPLY:
      return commentMarshaller.unmarshallReplyKey(
          (CommentReplyKey<?>) key, required);
    case DOCUMENT:
      return documentMarshaller.unmarshallDocumentKey(
          (DocumentKey) key, required);
    case DOCUMENT_CATEGORY:
      return documentMarshaller.unmarshallCategoryKey(
          (DocumentCategoryKey) key, required);
    case DOCUMENT_INSTANCE:
      return documentInstanceMarshaller
          .unmarshallDocumentInstanceKey((DocumentInstanceKey) key, required);
    case DOCUMENT_LABEL:
      return documentMarshaller.unmarshallLabelKey(
          (DocumentLabelKey) key, required);
    case DOCUMENT_PAGE:
      return documentInstanceMarshaller
          .unmarshallPageKey((DocumentPageKey) key, required);
    case DOCUMENT_VIEWER_ITEM:
      return documentInstanceMarshaller
          .unmarshallViewerItemKey((DocumentViewerItemKey) key, required);
    case MANAGEMENT_WORKSPACE:
      return managementWorkspaceMarshaller
          .unmarshallWorkspaceKey((ManagementWorkspaceKey) key, required);
    case SHARED_DOCUMENT:
      return collaborationWorkspaceMarshaller
          .unmarshallSharedDocumentKey((SharedDocumentKey) key, required);
    case SOCIAL_ACTOR:
      return actorMarshaller.unmarshallActorKey(
          (SocialActorKey) key, required);
    case TASK:
      return collaborationWorkspaceMarshaller
          .unmarshallTaskKey((TaskKey) key, required);
    case DISPLAY_NOTICE:
      return displayNoticeMarshaller
          .unmarshallDisplayNoticeKey((DisplayNoticeKey) key, true);
    default:
      throw new UnsupportedOperationException("Not supported: " + type.name());
    }
  }

  public <T extends IDomainEntity> T unmarshallKey(IKey key, boolean required,
      Class<? super T> bound) {
    IDomainEntity entity = unmarshallKey(key, required);
    if (entity != null && !bound.isInstance(entity)) {
      throw new InvalidKeyException("Key " + key
          + " does not refer to a " + bound.getSimpleName() + " but a "
          + entity.getClass().getSimpleName());
    }
    @SuppressWarnings("unchecked")
    T casted = (T) entity;
    return casted;
  }

  private IKey marshallKey(IDomainEntity entity) {
    EntityType type = entity.getEntityType();
    switch (type) {
    case COLLABORATION_WORKSPACE:
      return collaborationWorkspaceMarshaller
          .marshallWorkspaceKey((CollaborationWorkspace) entity);
    case COLLABORATION_WORKSPACE_PARTICIPATION:
      return collaborationWorkspaceMarshaller
          .marshallParticipationKey(
              (CollaborationWorkspaceParticipation) entity);
    case COMMENT:
      return commentMarshaller.marshallCommentKey(
          (Comment) entity);
    case COMMENT_REPLY:
      return commentMarshaller.marshallReplyKey(
          (CommentReply) entity);
    case DOCUMENT:
      return documentMarshaller.marshallDocumentKey(
          (Document) entity);
    case DOCUMENT_CATEGORY:
      return documentMarshaller.marshallCategoryKey(
          (DocumentCategory) entity);
    case DOCUMENT_INSTANCE:
      return documentInstanceMarshaller
          .marshallDocumentInstanceKey((DocumentInstance) entity);
    case DOCUMENT_LABEL:
      return documentMarshaller.marshallLabelKey(
          (DocumentLabel) entity);
    case DOCUMENT_PAGE:
      return documentInstanceMarshaller.marshallPageKey(
          (DocumentPage) entity);
    case DOCUMENT_VIEWER_ITEM:
      return documentInstanceMarshaller
          .marshallViewerItemKey((DocumentViewerItem) entity);
    case MANAGEMENT_WORKSPACE:
      return managementWorkspaceMarshaller
          .marshallWorkspaceKey((ManagementWorkspace) entity);
    case SHARED_DOCUMENT:
      return collaborationWorkspaceMarshaller
          .marshallSharedDocumentKey((SharedDocument) entity);
    case SOCIAL_ACTOR:
      return actorMarshaller.marshallActorKey(
          (SocialActor) entity);
    case TASK:
      return collaborationWorkspaceMarshaller
          .marshallTaskKey((Task) entity);
    case DISPLAY_NOTICE:
      return displayNoticeMarshaller
          .marshallDisplayNoticeKey((DisplayNotice) entity);
    default:
      throw new UnsupportedOperationException("Not supported: " + type.name());
    }
  }

  public <T extends IKey> T marshallKey(IDomainEntity entity,
      Class<? super T> bound) {
    IKey key = marshallKey(entity);
    if (!bound.isInstance(key)) {
      throw new InvalidKeyException("Entity "
          + entity.getClass().getSimpleName() + ":" + entity.getId()
          + " does not have a " + bound.getSimpleName() + " key but to "
          + key.getClass().getSimpleName());
    }
    @SuppressWarnings("unchecked")
    T casted = (T) key;
    return casted;
  }

  public <T extends IKey> T marshallKey(GenericReference<?> reference,
      Class<? super T> bound) {
    IDomainEntity entity = findOne(reference);
    IKey key = marshallKey(entity);
    if (!bound.isInstance(key)) {
      throw new InvalidKeyException("Reference " + reference.getType()
          + ":" + reference.getId() + " does not have a "
          + bound.getSimpleName() + " key but a "
          + key.getClass().getSimpleName());
    }
    @SuppressWarnings("unchecked")
    T casted = (T) key;
    return casted;
  }

  private IKeyHolderDto<?> marshall(IDomainEntity entity) {
    EntityType type = entity.getEntityType();
    switch (type) {
    case COLLABORATION_WORKSPACE:
      return collaborationWorkspaceMarshaller
          .marshallWorkspace((CollaborationWorkspace) entity);
    case COLLABORATION_WORKSPACE_PARTICIPATION:
      return collaborationWorkspaceMarshaller
          .marshallParticipation((CollaborationWorkspaceParticipation) entity);
    case COMMENT:
      return commentMarshaller.marshallComment(
          (Comment) entity);
    case COMMENT_REPLY:
      return commentMarshaller.marshallReply(
          (CommentReply) entity);
    case DOCUMENT:
      return documentMarshaller.marshallDocument(
          (Document) entity);
    case DOCUMENT_CATEGORY:
      return documentMarshaller.marshallCategory(
          (DocumentCategory) entity);
    case DOCUMENT_INSTANCE:
      return documentInstanceMarshaller
          .marshallDocumentInstance((DocumentInstance) entity);
    case DOCUMENT_LABEL:
      return documentMarshaller.marshallLabel(
          (DocumentLabel) entity);
    case DOCUMENT_PAGE:
      return documentInstanceMarshaller.marshallPage(
          (DocumentPage) entity);
    case DOCUMENT_VIEWER_ITEM:
      return documentInstanceMarshaller
          .marshallViewerItem((DocumentViewerItem) entity);
    case MANAGEMENT_WORKSPACE:
      return managementWorkspaceMarshaller
          .marshallWorkspace((ManagementWorkspace) entity);
    case SHARED_DOCUMENT:
      return collaborationWorkspaceMarshaller
          .marshallSharedDocument((SharedDocument) entity);
    case SOCIAL_ACTOR:
      return actorMarshaller.marshallActor(
          (SocialActor) entity);
    case TASK:
      return collaborationWorkspaceMarshaller
          .marshallTask((Task) entity);
    case DISPLAY_NOTICE:
      return displayNoticeMarshaller
          .marshallDisplayNotice((DisplayNotice) entity);
    default:
      throw new UnsupportedOperationException("Not supported: " + type.name());
    }
  }

  public <T extends IKey> IKeyHolderDto<T> marshall(IDomainEntity entity,
      Class<? super T> bound) {
    IKeyHolderDto<?> dto = marshall(entity);
    IKey key = dto.getKey();
    if (!bound.isInstance(key)) {
      throw new InvalidKeyException("Entity "
          + entity.getClass().getSimpleName() + ":" + entity.getId()
          + " does not have a " + bound.getSimpleName() + " key but to "
          + key.getClass().getSimpleName());
    }
    @SuppressWarnings("unchecked")
    IKeyHolderDto<T> casted = (IKeyHolderDto<T>) dto;
    return casted;
  }

  public <T extends IKey> IKeyHolderDto<T> marshall(
      GenericReference<?> reference, Class<? super T> bound) {
    IDomainEntity entity = findOne(reference);
    IKeyHolderDto<?> dto = marshall(entity);
    IKey key = dto.getKey();
    if (!bound.isInstance(key)) {
      throw new InvalidKeyException("Reference " + reference.getType()
          + ":" + reference.getId() + " does not have a "
          + bound.getSimpleName() + " key but a "
          + key.getClass().getSimpleName());
    }
    @SuppressWarnings("unchecked")
    IKeyHolderDto<T> casted = (IKeyHolderDto<T>) dto;
    return casted;
  }

  private IDomainEntity findOne(GenericReference<?> reference) {
    EntityType type = reference.getType();
    Long id = reference.getId();
    switch (type) {
    case COLLABORATION_WORKSPACE:
      return collaborationWorkspaceRepository.findOne(id);
    case COLLABORATION_WORKSPACE_PARTICIPATION:
      return collaborationWorkspaceParticipationRepository.findOne(id);
    case COMMENT:
      return commentRepository.findOne(id);
    case COMMENT_REPLY:
      return commentReplyRepository.findOne(id);
    case DOCUMENT:
      return documentRepository.findOne(id);
    case DOCUMENT_CATEGORY:
      return documentCategoryRepository.findOne(id);
    case DOCUMENT_INSTANCE:
      return documentInstanceRepository.findOne(id);
    case DOCUMENT_LABEL:
      return documentLabelRepository.findOne(id);
    case DOCUMENT_PAGE:
      return documentPageRepository.findOne(id);
    case DOCUMENT_VIEWER_ITEM:
      return documentViewerItemRepository.findOne(id);
    case MANAGEMENT_WORKSPACE:
      return managementWorkspaceRepository.findOne(id);
    case SHARED_DOCUMENT:
      return sharedDocumentRepository.findOne(id);
    case SOCIAL_ACTOR:
      return socialActorRepository.findOne(id);
    case TASK:
      return this.taskRepository.findOne(id);
    case DISPLAY_NOTICE:
      return this.displayNoticeRepository.findOne(id);
    default:
      throw new UnsupportedOperationException("Not supported: " + type.name());
    }
  }

  public <T extends IDomainEntity> T findOne(GenericReference<?> reference,
      Class<? super T> bound) {
    IDomainEntity entity = findOne(reference);
    if (!bound.isInstance(entity)) {
      throw new InvalidKeyException("Reference " + reference.getType()
          + ":" + reference.getId() + " does not have a "
          + bound.getSimpleName() + " entity but a "
          + entity.getClass().getSimpleName());
    }
    @SuppressWarnings("unchecked")
    T casted = (T) entity;
    return casted;
  }
}
