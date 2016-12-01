package com.similan.service.impl.wall.entrymarshaller.comment;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.comment.ICommentable;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.wall.comment.CommentPostedWallEntry;
import com.similan.service.api.base.dto.basic.IKeyHolderDto;
import com.similan.service.api.comment.dto.basic.CommentDto;
import com.similan.service.api.comment.dto.key.ICommentableKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.comment.CommentPostedEntryDto;
import com.similan.service.api.wall.dto.key.IWallContainerKey;
import com.similan.service.api.wall.dto.key.WallEntryKey;
import com.similan.service.impl.comment.CommentMarshaller;
import com.similan.service.impl.common.GenericReferenceMarshaller;
import com.similan.service.impl.wall.WallEntryMarshaller;

@Component
public class CommentReplyPostedEntryMarshaller extends
		WallEntryMarshaller<CommentPostedWallEntry, IWallContainerKey> {
  @Autowired
  private CommentMarshaller commentMarshaller;
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;
  
	@Override
	public WallEntryDto<IWallContainerKey> marshall(
			WallEntryKey<IWallContainerKey> entryKey,
			ActorDto initiatorDto, Date date,
			CommentPostedWallEntry entry) {
		Comment comment = entry.getComment();
		GenericReference<ICommentable> commentableReference = comment
				.getCommentable();
		CommentDto<ICommentableKey> commentDto = commentMarshaller.marshallComment(comment);
		IKeyHolderDto<ICommentableKey> commentableDto = genericReferenceMarshaller.marshall(commentableReference,
						ICommentableKey.class);
		CommentPostedEntryDto<IWallContainerKey> entryDto = new CommentPostedEntryDto<>(
				entryKey, initiatorDto, date, commentableDto, commentDto);
		return entryDto;
	}

}
