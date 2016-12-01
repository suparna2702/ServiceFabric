package com.similan.domain.repository.wall.contentspace;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.contentworkspace.ContentWorkspaceDocumentCheckInWallEntry;
import com.similan.domain.entity.wall.contentworkspace.ContentWorkspaceDocumentCheckOutWallEntry;
import com.similan.domain.entity.wall.contentworkspace.ContentWorkspaceDocumentCommentWallEntry;
import com.similan.domain.entity.wall.contentworkspace.ContentWorkspaceDocumentDownloadedWallEntry;
import com.similan.domain.entity.wall.contentworkspace.ContentWorkspaceDocumentExternalSharedWallEntry;
import com.similan.domain.entity.wall.contentworkspace.ContentWorkspaceDocumentInNetworkSharedWallEntry;
import com.similan.domain.entity.wall.contentworkspace.ContentWorkspaceDocumentUpdateWallEntry;
import com.similan.domain.entity.wall.contentworkspace.ContentWorkspaceDocumentUploadedWallEntry;
import com.similan.domain.entity.wall.contentworkspace.ContentWorkspaceDocumentViewedWallEntry;
import com.similan.domain.entity.wall.contentworkspace.ContentWorkspaceDocumentWallEntry;
import com.similan.domain.entity.wall.contentworkspace.ContentWorkspaceWallEntry;
import com.similan.domain.repository.common.GenericReferenceRepository;
import com.similan.domain.repository.wall.AbstractWallEntryRepository;
import com.similan.domain.share.ExternalShared;
import com.similan.domain.share.InNetworkShared;

@Repository
public class ContentWorkspaceWallEntryRepository extends
    AbstractWallEntryRepository<ContentWorkspaceWallEntry> {

  @Autowired
  private GenericReferenceRepository genericReferenceRepository;

  private void setContentWorkspaceDocumentWallEntryProperty(
      ContentWorkspaceDocumentWallEntry entry, Document document) {
    GenericReference<IWallEntrySubject> subject = genericReferenceRepository
        .create((IWallEntrySubject) document);
    entry.setSubject(subject);

    entry.setDocumentVersion(document.getLastInstance().getVersion());
  }

  public ContentWorkspaceDocumentCheckInWallEntry createDocumentCheckInWallEntry(
      Wall wall, Document document, SocialActor viewer) {
    int number = getNextNumber(wall);
    Date date = new Date();

    ContentWorkspaceDocumentCheckInWallEntry entry = new ContentWorkspaceDocumentCheckInWallEntry(
        number, date, document.getLastInstance().getVersion());

    entry.setInitiator(viewer);
    setContentWorkspaceDocumentWallEntryProperty(entry, document);
    created(wall, viewer, entry);

    return entry;
  }

  public ContentWorkspaceDocumentCheckOutWallEntry createDocumentCheckOutWallEntry(
      Wall wall, Document document, SocialActor viewer) {

    int number = getNextNumber(wall);
    Date date = new Date();

    ContentWorkspaceDocumentCheckOutWallEntry entry = new ContentWorkspaceDocumentCheckOutWallEntry(
        number, date, document.getLastInstance().getVersion());

    entry.setInitiator(viewer);
    setContentWorkspaceDocumentWallEntryProperty(entry, document);
    created(wall, viewer, entry);

    return entry;
  }

  public ContentWorkspaceDocumentCommentWallEntry createDocumentCommentWallEntry(
      Wall wall, Document document, SocialActor viewer, Comment comment) {
    int number = getNextNumber(wall);
    Date date = new Date();

    ContentWorkspaceDocumentCommentWallEntry entry = new ContentWorkspaceDocumentCommentWallEntry(
        number, date, document.getLastInstance().getVersion(), comment);
    entry.setInitiator(viewer);
    setContentWorkspaceDocumentWallEntryProperty(entry, document);
    created(wall, viewer, entry);

    return entry;
  }

  public ContentWorkspaceDocumentViewedWallEntry createDocumentViewedWallEntry(
      Wall wall, Document document, SocialActor viewer) {

    int number = getNextNumber(wall);
    Date date = new Date();

    ContentWorkspaceDocumentViewedWallEntry entry = new ContentWorkspaceDocumentViewedWallEntry(
        number, date, document.getLastInstance().getVersion());
    entry.setInitiator(viewer);
    setContentWorkspaceDocumentWallEntryProperty(entry, document);
    created(wall, viewer, entry);

    return entry;
  }

  public ContentWorkspaceDocumentUploadedWallEntry createDocumentUploadWallEntry(
      Wall wall, Document document, SocialActor uploader) {

    int number = getNextNumber(wall);
    Date date = new Date();

    ContentWorkspaceDocumentUploadedWallEntry entry = new ContentWorkspaceDocumentUploadedWallEntry(
        number, date, document.getLastInstance().getVersion());
    entry.setInitiator(uploader);
    setContentWorkspaceDocumentWallEntryProperty(entry, document);
    created(wall, uploader, entry);

    return entry;
  }

  public ContentWorkspaceDocumentDownloadedWallEntry createDocumentDownloadWallEntry(
      Wall wall, Document document, SocialActor downloader) {

    int number = getNextNumber(wall);
    Date date = new Date();

    ContentWorkspaceDocumentDownloadedWallEntry entry = new ContentWorkspaceDocumentDownloadedWallEntry(
        number, date, document.getLastInstance().getVersion());
    entry.setInitiator(downloader);
    setContentWorkspaceDocumentWallEntryProperty(entry, document);
    created(wall, downloader, entry);

    return entry;
  }

  public ContentWorkspaceDocumentExternalSharedWallEntry createContentWorkspaceDocumentExternalSharedWallEntry(
      Wall wall, ExternalShared shared, Document document) {
    int number = getNextNumber(wall);
    Date date = new Date();

    ContentWorkspaceDocumentExternalSharedWallEntry wallEntry = new ContentWorkspaceDocumentExternalSharedWallEntry(
        shared, number, date, document.getLastInstance().getVersion());
    this.setContentWorkspaceDocumentWallEntryProperty(wallEntry, document);
    created(wall, shared.getSharedBy(), wallEntry);

    return wallEntry;
  }

  public ContentWorkspaceDocumentInNetworkSharedWallEntry createContentWorkspaceDocumentInNetworkSharedWallEntry(
      Wall wall, InNetworkShared share, Document document) {

    int number = getNextNumber(wall);
    Date date = new Date();

    ContentWorkspaceDocumentInNetworkSharedWallEntry wallEntry = new ContentWorkspaceDocumentInNetworkSharedWallEntry(
        share, number, date, document.getLastInstance().getVersion());
    this.setContentWorkspaceDocumentWallEntryProperty(wallEntry, document);
    created(wall, share.getSharedBy(), wallEntry);

    return wallEntry;
  }

  public ContentWorkspaceDocumentUpdateWallEntry createDocumentUpdateWallEntry(
      Wall wall, Document document, SocialActor updator) {

    int number = getNextNumber(wall);
    Date date = new Date();

    ContentWorkspaceDocumentUpdateWallEntry wallEntry = new ContentWorkspaceDocumentUpdateWallEntry(
        number, date, document.getLastInstance().getVersion());
    wallEntry.setInitiator(updator);
    setContentWorkspaceDocumentWallEntryProperty(wallEntry, document);
    created(wall, updator, wallEntry);

    return wallEntry;
  }

}
