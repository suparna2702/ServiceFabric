package com.similan.domain.repository.wall.comment;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.comment.CommentReply;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.WallEntry;
import com.similan.domain.entity.wall.comment.CommentPostedWallEntry;
import com.similan.domain.entity.wall.comment.CommentReplyPostedWallEntry;
import com.similan.domain.repository.wall.AbstractWallEntryRepository;

@Repository
public class CommentWallEntryRepository extends
    AbstractWallEntryRepository<WallEntry> {

  public CommentPostedWallEntry createCommentPostedEntry(Wall wall,
      Comment comment) {
	  
    int number = getNextNumber(wall);
    Date date = new Date();

    CommentPostedWallEntry entry = new CommentPostedWallEntry(number,
            date);
    
    entry.setComment(comment);
    SocialActor initiator = comment.getAuthor();
    created(wall, initiator, entry);
    
    return entry;
  }

  public CommentReplyPostedWallEntry createCommentReplyPostedEntry(Wall wall,
      CommentReply commentReply) {
    int number = getNextNumber(wall);
    Date date = new Date();

    CommentReplyPostedWallEntry entry = new CommentReplyPostedWallEntry(number, date);
    entry.setCommentReply(commentReply);
    SocialActor initiator = commentReply.getAuthor();
    created(wall, initiator, entry);
    return entry;
  }

}
