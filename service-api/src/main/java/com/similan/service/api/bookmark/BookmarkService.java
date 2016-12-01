package com.similan.service.api.bookmark;

import java.net.URISyntaxException;
import java.util.List;

import com.similan.service.api.bookmark.dto.basic.BookmarkDto;
import com.similan.service.api.bookmark.dto.key.BookmarkKey;
import com.similan.service.api.bookmark.dto.key.IBookmarkableKey;
import com.similan.service.api.bookmark.dto.operation.NewBookmarkDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

public interface BookmarkService {

  public boolean isBookmarked(SocialActorKey owner, IBookmarkableKey bookmarkObj);

  public BookmarkDto create(NewBookmarkDto newBookmarkDto)
      throws URISyntaxException;

  public BookmarkDto get(BookmarkKey bookmarkKey) throws URISyntaxException;

  public boolean delete(BookmarkKey bookmarkKey);

  public List<BookmarkDto> getByOwner(SocialActorKey ownerKey)
      throws URISyntaxException;

}
