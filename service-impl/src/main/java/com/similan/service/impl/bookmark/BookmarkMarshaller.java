package com.similan.service.impl.bookmark;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.bookmark.Bookmark;
import com.similan.domain.entity.bookmark.IBookmarkable;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.repository.bookmark.BookmarkRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.service.api.base.dto.basic.IKeyHolderDto;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.bookmark.dto.basic.BookmarkDto;
import com.similan.service.api.bookmark.dto.error.BookmarkErrorCode;
import com.similan.service.api.bookmark.dto.error.BookmarkException;
import com.similan.service.api.bookmark.dto.key.BookmarkKey;
import com.similan.service.api.bookmark.dto.key.IBookmarkableKey;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.key.SharedDocumentKey;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.impl.Marshaller;
import com.similan.service.impl.common.GenericReferenceMarshaller;

@Slf4j
@Component
public class BookmarkMarshaller extends Marshaller {
  @Autowired
  private PlatformCommonSettings platformCommonSettings;
  @Autowired
  private BookmarkRepository bookmarkRepository;
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;

  public Bookmark unmarshallBookmarkKey(BookmarkKey bookmarkKey,
      boolean required) {

    Long bookMarkId = bookmarkKey.getId();
    Bookmark bookmark = bookmarkRepository.findOne(bookMarkId);

    if (bookmark == null && required) {
      throw new BookmarkException(BookmarkErrorCode.BOOKMARK_NOT_FOUND, "Bookmark key " + bookmarkKey
          + "  not found.");
    }
    return bookmark;
  }

  public BookmarkDto marshallBookmark(Bookmark bookmark)
      throws URISyntaxException {
    BookmarkKey bookmarkKey = new BookmarkKey(bookmark.getId());
    BookmarkDto bookmarkDto = new BookmarkDto(bookmarkKey, bookmark
        .getBookmarkable().getType(), bookmark.getCreationDate(),
        bookmark.getName(), this.getBookMarkUrl(bookmark));

    log.info("Marshalled bookmark " + bookmarkDto);

    return bookmarkDto;

  }

  public List<BookmarkDto> marshallBookmarks(List<Bookmark> bookmarks)
      throws URISyntaxException {

    List<BookmarkDto> bookmarkList = new ArrayList<BookmarkDto>();

    for (Bookmark bookmark : bookmarks) {
      BookmarkDto bookmarkDto = this.marshallBookmark(bookmark);
      bookmarkList.add(bookmarkDto);
    }

    return bookmarkList;
  }

  private String getBookMarkUrl(Bookmark bookmark) throws URISyntaxException {
    GenericReference<IBookmarkable> bookmaredObject = bookmark
        .getBookmarkable();

    EntityType entityType = bookmaredObject.getType();
    String hostUrl = this.platformCommonSettings.getPortalApplcationUrl()
        .getValue();

    switch (entityType) {
    case COLLABORATION_WORKSPACE: {

      IKeyHolderDto<CollaborationWorkspaceKey> collaborationWorkspaceReferenceKey =
          genericReferenceMarshaller
          .marshall(bookmaredObject, IBookmarkableKey.class);
      CollaborationWorkspaceKey collaborationWorkspaceKey = collaborationWorkspaceReferenceKey
          .getKey();

      URI uri = new URIBuilder(hostUrl + "collabspace/workspaceDetail.xhtml")
          .setParameter("wsname", collaborationWorkspaceKey.getName())
          .setParameter("owsname",
              collaborationWorkspaceKey.getOwner().getName()).build();

      String retUrl = uri.toString();
      log.info("Collaboration view url " + retUrl);
      return retUrl;

    }
    case DOCUMENT: {
      IKeyHolderDto<DocumentKey> documentReferenceKey =
          genericReferenceMarshaller
          .marshall(bookmaredObject, IBookmarkableKey.class);
      DocumentKey documentKey = documentReferenceKey.getKey();

      URI uri = new URIBuilder(hostUrl + "docspace/viewDocumentDetails.xhtml")
          .setParameter("dname", documentKey.getName())
          .setParameter("msname", documentKey.getWorkspace().getName()).build();

      String retUrl = uri.toString();
      log.info("Document view url " + retUrl);
      return retUrl;

    }
    case SHARED_DOCUMENT: {
      IKeyHolderDto<SharedDocumentKey> sharedDocumentReferenceKey =
          genericReferenceMarshaller
          .marshall(bookmaredObject, IBookmarkableKey.class);
      SharedDocumentKey sharedDocumentKey = sharedDocumentReferenceKey.getKey();

      URI uri = new URIBuilder(hostUrl
          + "collabspace/workspaceDocumentDetail.xhtml").setParameter("sdoc",
          sharedDocumentKey.getId().toString()).build();

      String retUrl = uri.toString();
      log.info("Document view url " + retUrl);
      return retUrl;

    }
    default: {
        return StringUtils.EMPTY;
      }
    }
  }

}
