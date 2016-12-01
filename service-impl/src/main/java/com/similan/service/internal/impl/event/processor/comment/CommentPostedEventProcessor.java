package com.similan.service.internal.impl.event.processor.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.comment.ICommentable;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.common.IDomainEntity;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.comment.CommentPostedWallEntry;
import com.similan.domain.repository.comment.CommentRepository;
import com.similan.domain.repository.common.GenericReferenceRepository;
import com.similan.domain.repository.wall.comment.CommentWallEntryRepository;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.impl.common.GenericReferenceMarshaller;
import com.similan.service.internal.api.event.io.comment.CommentPostedEvent;

@Component
public class CommentPostedEventProcessor extends
    AbstractCommentPostedEventProcessor<CommentPostedEvent> {
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private CommentWallEntryRepository commentWallEntryRepository;
  @Autowired
  private GenericReferenceRepository genericReferenceRepository;
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;

  @Override
  public void preProcess(CommentPostedEvent event) {
    Comment comment = this.commentRepository.findOne(event.getCommentId());
    Wall wall = getWall(comment);
    
    if (wall == null) {
      return;
    }
    
    CommentPostedWallEntry commentEntry = this.commentWallEntryRepository
        .createCommentPostedEntry(wall, comment);
    
    /* set the proper subject of the comment */
    EntityType entityType = comment.getCommentable().getType();
    IDomainEntity subjectEntity = genericReferenceMarshaller
    		.findOne(comment.getCommentable(), 
    				ICommentable.class);
    
    switch(entityType){
       case SHARED_DOCUMENT: {
    	    GenericReference<IWallEntrySubject> subject = genericReferenceRepository
    		        .create((IWallEntrySubject)subjectEntity);
    	    commentEntry.setSubject(subject);
    	    break;
       
        }
    }

    post(commentEntry);
  }

}
