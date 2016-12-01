package com.similan.domain.repository.bookmark;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.bookmark.Bookmark;
import com.similan.domain.entity.bookmark.IBookmarkable;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.repository.bookmark.jpa.JpaBookmarkRepository;
import com.similan.domain.repository.common.GenericReferenceRepository;
import com.similan.service.api.base.dto.key.EntityType;

@Repository
public class BookmarkRepository {
  @Autowired
  private JpaBookmarkRepository repository;

  @Autowired
  private GenericReferenceRepository genericReferenceRepository;

  public Bookmark save(Bookmark entity) {
    return repository.save(entity);
  }

  public Bookmark findOne(Long id) {
    return repository.findOne(id);
  }

  public void delete(Long id) {
    this.repository.delete(id);
  }

  public Bookmark findOne(String ownerName, EntityType commentableType,
      String name) {
    return repository.findOne(ownerName, commentableType, name);
  }

  public List<Bookmark> findByBookmarkable(EntityType commentableType,
      long bookmarkableId) {
    return repository.findByBookmarkable(commentableType, bookmarkableId);
  }

  public List<Bookmark> findByOwner(SocialActor owner) {
    return repository.findByOwner(owner);
  }

  public List<Bookmark> findByOwnerAndBookmarkable(SocialActor owner,
      EntityType bookmarkableType, long bookmarkableId) {
    return repository.findByOwnerAndBookmarkable(owner, bookmarkableType,
        bookmarkableId);
  }

  public Bookmark create(SocialActor owner, IBookmarkable bookmarkable,
      String name) {
    Date date = new Date();

    GenericReference<IBookmarkable> bookmarkableReference = genericReferenceRepository
        .create(bookmarkable);

    Bookmark bookmark = new Bookmark(name, date);
    bookmark.setOwner(owner);
    bookmark.setBookmarkable(bookmarkableReference);
    bookmark.setName(name);

    return bookmark;
  }

}
