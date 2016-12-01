package com.similan.domain.entity.comment;

import com.similan.domain.entity.common.IDomainEntity;

public interface ICommentable extends IDomainEntity {

  void setLastComment(Comment lastComment);

  Comment getLastComment();
}
