package com.similan.service.impl.bookmark;

import java.net.URISyntaxException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.bookmark.Bookmark;
import com.similan.domain.entity.bookmark.IBookmarkable;
import com.similan.domain.entity.common.IDomainEntity;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.repository.bookmark.BookmarkRepository;
import com.similan.service.api.bookmark.BookmarkService;
import com.similan.service.api.bookmark.dto.basic.BookmarkDto;
import com.similan.service.api.bookmark.dto.key.BookmarkKey;
import com.similan.service.api.bookmark.dto.key.IBookmarkableKey;
import com.similan.service.api.bookmark.dto.operation.NewBookmarkDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.impl.common.GenericReferenceMarshaller;
import com.similan.service.impl.community.SocialActorMarshaller;

@Slf4j
@Service
public class BookmarkServiceImpl extends ServiceImpl implements BookmarkService {
  @Autowired
  private BookmarkRepository bookmarkRepository;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;
  @Autowired
  private BookmarkMarshaller bookmarkMarshaller;
  
  @Override
  @Transactional
  public BookmarkDto create(NewBookmarkDto newBookmarkDto)
      throws URISyntaxException {

    log.info("New bookmark " + newBookmarkDto);

    SocialActor owner = actorMarshaller
        .unmarshallActorKey(newBookmarkDto.getOwner(), true);

    IDomainEntity domainEntity = genericReferenceMarshaller
        .unmarshallKey(newBookmarkDto.getBookmarkable(), true);

    Bookmark bookmark = bookmarkRepository.create(owner,
        (IBookmarkable) domainEntity, newBookmarkDto.getName());
    bookmarkRepository.save(bookmark);

    BookmarkDto retBookmark = bookmarkMarshaller
        .marshallBookmark(bookmark);
    return retBookmark;
  }

  @Override
  @Transactional
  public BookmarkDto get(BookmarkKey bookmarkKey) throws URISyntaxException {

    Bookmark retBookmark = bookmarkMarshaller
        .unmarshallBookmarkKey(bookmarkKey, true);

    BookmarkDto retBookmarkDto = bookmarkMarshaller
        .marshallBookmark(retBookmark);

    return retBookmarkDto;
  }

  @Override
  @Transactional
  public boolean delete(BookmarkKey bookmarkKey) {

    log.info("deleting bookmark " + bookmarkKey);

    this.bookmarkRepository.delete(bookmarkKey.getId());

    return true;
  }

  @Override
  @Transactional
  public List<BookmarkDto> getByOwner(SocialActorKey ownerKey)
      throws URISyntaxException {

    log.info("Getting bookmarks by owner " + ownerKey);

    SocialActor owner = actorMarshaller
        .unmarshallActorKey(ownerKey, true);

    List<Bookmark> bookmarkList = this.bookmarkRepository.findByOwner(owner);

    List<BookmarkDto> bookmarkDtoList = bookmarkMarshaller.marshallBookmarks(bookmarkList);

    return bookmarkDtoList;
  }

  @Override
  @Transactional
  public boolean isBookmarked(SocialActorKey ownerKey,
      IBookmarkableKey bookmarkObjKey) {
    
    log.info("Evaluating bookmarks  " + bookmarkObjKey + " by owner "
        + ownerKey);

    SocialActor owner = actorMarshaller
        .unmarshallActorKey(ownerKey, true);

    IBookmarkable bookmarkable = (IBookmarkable) genericReferenceMarshaller.unmarshallKey(bookmarkObjKey, true);

    List<Bookmark> bookmarkList = this.bookmarkRepository
        .findByOwnerAndBookmarkable(owner, bookmarkable.getEntityType(),
            bookmarkable.getId());

    if (bookmarkList != null) {
      if (bookmarkList.size() > 0) {
        log.info(" Bookmark exists ");
        return true;
      }
    }

    return false;
  }

}
