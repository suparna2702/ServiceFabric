package com.similan.service.internal.impl.event.processor.comment;

import org.springframework.beans.factory.annotation.Autowired;

import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.comment.ICommentable;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.WallEntry;
import com.similan.service.impl.common.GenericReferenceMarshaller;
import com.similan.service.internal.api.event.io.Event;
import com.similan.service.internal.api.wall.WallInternalService;
import com.similan.service.internal.impl.event.EventPreProcessor;

public abstract class AbstractCommentPostedEventProcessor<T extends Event>
    implements EventPreProcessor<T> {
  @Autowired
  private WallInternalService wallInternalService;
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;

  protected Wall getWall(Comment comment) {
    GenericReference<ICommentable> commentableReference = comment
        .getCommentable();
    ICommentable commentable = genericReferenceMarshaller
        .findOne(commentableReference, ICommentable.class);
    Wall wall = wallInternalService.getWall(commentable);
    return wall;
  }

  protected void post(WallEntry entry) {
      this.wallInternalService.post(entry);
  }
}
