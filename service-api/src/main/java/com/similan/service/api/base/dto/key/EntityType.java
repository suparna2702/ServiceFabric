package com.similan.service.api.base.dto.key;

import static com.similan.service.api.base.dto.key.EntityPaths.PATH_ACTOR;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_AUDIT_EVENT;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_BOOKMARK;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_CATEGORY;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_CHAT_SESSION;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_COLLABORATION_WORKSPACE;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_COMMENT;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_COMMENT_REPLY;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_CONFERENCE;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_CONNECTION;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_DOCUMENT;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_DOCUMENT_INSTANCE;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_DOCUMENT_PAGE;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_DOCUMENT_VIEWER_ITEM;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_FEED;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_FEED_ENTRY;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_INVITE;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_LABEL;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_MANAGEMENT_WORKSPACE;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_PARTICIPATION;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_PRODUCT;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_PRODUCT_FEATURE;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_PRODUCT_TAG;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_SHARED_DOCUMENT;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_TAG;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_TAG_LINK;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_TASK;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_WALL;
import static com.similan.service.api.base.dto.key.EntityPaths.PATH_WALL_ENTRY;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_ACTORS;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_AUDIT_EVENT;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_BOOKMARKS;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_CATEGORIES;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_CHAT_SESSIONS;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_COLLABORATION_WORKSPACES;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_COMMENTS;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_COMMENT_REPLIES;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_CONFERENCES;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_CONNECTIONS;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_DOCUMENTS;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_DOCUMENT_INSTANCES;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_DOCUMENT_PAGES;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_DOCUMENT_VIEWER_ITEMS;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_FEEDS;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_FEED_ENTRIES;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_INVITES;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_LABELS;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_MANAGEMENT_WORKSPACES;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_PARTICIPATIONS;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_PRODUCTS;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_PRODUCT_FEATURES;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_PRODUCT_TAGS;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_SHARED_DOCUMENTS;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_TAG;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_TAG_LINK;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_TASKS;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_WALLS;
import static com.similan.service.api.base.dto.key.EntityPaths.UNIQUE_SEGMENT_WALL_ENTRIES;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.similan.service.api.advertisement.dto.key.DisplayNoticeKey;
import com.similan.service.api.audit.AuditEventKey;
import com.similan.service.api.base.dto.error.InvalidKeyException;
import com.similan.service.api.bookmark.dto.key.BookmarkKey;
import com.similan.service.api.chat.dto.key.ChatSessionKey;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceParticipationKey;
import com.similan.service.api.collaborationworkspace.dto.key.ConferenceKey;
import com.similan.service.api.collaborationworkspace.dto.key.InviteKey;
import com.similan.service.api.collaborationworkspace.dto.key.SharedDocumentKey;
import com.similan.service.api.collaborationworkspace.dto.key.TaskKey;
import com.similan.service.api.comment.dto.key.CommentKey;
import com.similan.service.api.comment.dto.key.CommentReplyKey;
import com.similan.service.api.comment.dto.key.ICommentableKey;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.connection.dto.key.ConnectionKey;
import com.similan.service.api.document.dto.key.DocumentCategoryKey;
import com.similan.service.api.document.dto.key.DocumentInstanceKey;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.key.DocumentLabelKey;
import com.similan.service.api.document.dto.key.DocumentPageKey;
import com.similan.service.api.document.dto.key.DocumentViewerItemKey;
import com.similan.service.api.feed.dto.key.FeedEntryKey;
import com.similan.service.api.feed.dto.key.FeedKey;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceParticipationKey;
import com.similan.service.api.product.dto.key.ProductFeatureKey;
import com.similan.service.api.product.dto.key.ProductKey;
import com.similan.service.api.product.dto.key.ProductTagKey;
import com.similan.service.api.tag.TagKey;
import com.similan.service.api.tag.TagLinkKey;
import com.similan.service.api.wall.dto.key.IWallContainerKey;
import com.similan.service.api.wall.dto.key.WallEntryKey;
import com.similan.service.api.wall.dto.key.WallKey;

public enum EntityType {

  TAGGED_ENTITY(UNIQUE_SEGMENT_TAG_LINK, PATH_TAG_LINK) {
    @Override
    protected String[] doGetValues(IKey key) {
      TagLinkKey casted = (TagLinkKey) key;
      return new String[] { String.valueOf(casted.getId()) };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new TagLinkKey(Long.valueOf(values[0]));
    }
  },

  EXTERNAL_CONTENT(UNIQUE_SEGMENT_TAG_LINK, PATH_TAG_LINK) {
    @Override
    protected String[] doGetValues(IKey key) {
      TagLinkKey casted = (TagLinkKey) key;
      return new String[] { String.valueOf(casted.getId()) };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new TagLinkKey(Long.valueOf(values[0]));
    }
  },

  TAG_LINK(UNIQUE_SEGMENT_TAG_LINK, PATH_TAG_LINK) {
    @Override
    protected String[] doGetValues(IKey key) {
      TagLinkKey casted = (TagLinkKey) key;
      return new String[] { String.valueOf(casted.getId()) };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new TagLinkKey(Long.valueOf(values[0]));
    }
  },

  TAG(UNIQUE_SEGMENT_TAG, PATH_TAG) {
    @Override
    protected String[] doGetValues(IKey key) {
      TagKey casted = (TagKey) key;
      return new String[] { String.valueOf(casted.getId()) };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new TagKey(Long.valueOf(values[0]));
    }
  },

  AUDIT_EVENT(UNIQUE_SEGMENT_AUDIT_EVENT, PATH_AUDIT_EVENT) {
    @Override
    protected String[] doGetValues(IKey key) {
      AuditEventKey casted = (AuditEventKey) key;
      return new String[] { String.valueOf(casted.getId()) };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new AuditEventKey(Long.valueOf(values[0]));
    }
  },

  BOOKMARK(UNIQUE_SEGMENT_BOOKMARKS, PATH_BOOKMARK) {
    @Override
    protected String[] doGetValues(IKey key) {
      BookmarkKey casted = (BookmarkKey) key;
      return new String[] { String.valueOf(casted.getId()) };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new BookmarkKey(Long.valueOf(values[0]));
    }
  },

  CHAT_SESSION(UNIQUE_SEGMENT_CHAT_SESSIONS, PATH_CHAT_SESSION) {
    @Override
    protected String[] doGetValues(IKey key) {
      ChatSessionKey casted = (ChatSessionKey) key;
      return new String[] { casted.getInitiator().getName(),
          casted.getFirstInvitee().getName(), formatTimestamp(casted.getDate()) };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new ChatSessionKey(values[0], values[1],
          valueOfTimestamp(values[2]));
    }
  },

  COLLABORATION_WORKSPACE_SURVEY(
      UNIQUE_SEGMENT_COLLABORATION_WORKSPACES,
      PATH_COLLABORATION_WORKSPACE) {
    @Override
    protected String[] doGetValues(IKey key) {
      CollaborationWorkspaceKey casted = (CollaborationWorkspaceKey) key;
      return new String[] { casted.getOwner().getName(), casted.getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new CollaborationWorkspaceKey(values[0], values[1]);
    }
  },

  COLLABORATION_WORKSPACE(
      UNIQUE_SEGMENT_COLLABORATION_WORKSPACES,
      PATH_COLLABORATION_WORKSPACE) {

    @Override
    protected String[] doGetValues(IKey key) {
      CollaborationWorkspaceKey casted = (CollaborationWorkspaceKey) key;
      return new String[] { casted.getOwner().getName(), casted.getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new CollaborationWorkspaceKey(values[0], values[1]);
    }
  },

  COMMENT(UNIQUE_SEGMENT_COMMENTS, PATH_COMMENT) {

    @Override
    protected String[] doGetValues(IKey key) {
      CommentKey<?> casted = (CommentKey<?>) key;
      return new String[] { casted.getNumber().toString() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new CommentKey<ICommentableKey>(
          (ICommentableKey) genericContainerKey, Integer.valueOf(values[0]));
    }

    @Override
    protected IKey doGetContainerKey(IKey key) {
      return ((CommentKey<?>) key).getCommentable();
    }

  },

  COMMENT_REPLY(UNIQUE_SEGMENT_COMMENT_REPLIES, PATH_COMMENT_REPLY) {
    @Override
    protected String[] doGetValues(IKey key) {
      CommentReplyKey<?> casted = (CommentReplyKey<?>) key;
      return new String[] { casted.getComment().getNumber().toString(),
          casted.getNumber().toString() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new CommentReplyKey<ICommentableKey>(
          (ICommentableKey) genericContainerKey, Integer.valueOf(values[0]),
          Integer.valueOf(values[1]));
    }

    @Override
    protected IKey doGetContainerKey(IKey key) {
      return ((CommentReplyKey<?>) key).getComment().getCommentable();
    }
  },

  CONFERENCE(UNIQUE_SEGMENT_CONFERENCES, PATH_CONFERENCE) {
    @Override
    protected String[] doGetValues(IKey key) {
      ConferenceKey casted = (ConferenceKey) key;
      return new String[] { casted.getWorkspace().getOwner().getName(),
          casted.getWorkspace().getName(), casted.getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new ConferenceKey(values[0], values[1], values[2]);
    }
  },

  DOCUMENT(UNIQUE_SEGMENT_DOCUMENTS, PATH_DOCUMENT) {
    @Override
    protected String[] doGetValues(IKey key) {
      DocumentKey casted = (DocumentKey) key;
      return new String[] { casted.getWorkspace().getOwner().getName(),
          casted.getWorkspace().getName(), casted.getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new DocumentKey(values[0], values[1], values[2]);
    }
  },

  DOCUMENT_CATEGORY(UNIQUE_SEGMENT_CATEGORIES, PATH_CATEGORY) {
    @Override
    protected String[] doGetValues(IKey key) {
      DocumentCategoryKey casted = (DocumentCategoryKey) key;
      return new String[] { casted.getOwner().getName(), casted.getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new DocumentLabelKey(values[0], values[1]);
    }
  },

  DOCUMENT_INSTANCE(UNIQUE_SEGMENT_DOCUMENT_INSTANCES, PATH_DOCUMENT_INSTANCE) {
    @Override
    protected String[] doGetValues(IKey key) {
      DocumentInstanceKey casted = (DocumentInstanceKey) key;
      return new String[] {
          casted.getDocument().getWorkspace().getOwner().getName(),
          casted.getDocument().getWorkspace().getName(),
          casted.getDocument().getName(), casted.getVersion().toString() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new DocumentInstanceKey(values[0], values[1], values[2],
          Integer.valueOf(values[3]));
    }
  },

  DOCUMENT_LABEL(UNIQUE_SEGMENT_LABELS, PATH_LABEL) {
    @Override
    protected String[] doGetValues(IKey key) {
      DocumentLabelKey casted = (DocumentLabelKey) key;
      return new String[] { casted.getOwner().getName(), casted.getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new DocumentLabelKey(values[0], values[1]);
    }
  },

  DOCUMENT_PAGE(UNIQUE_SEGMENT_DOCUMENT_PAGES, PATH_DOCUMENT_PAGE) {
    @Override
    protected String[] doGetValues(IKey key) {
      DocumentPageKey casted = (DocumentPageKey) key;
      return new String[] {
          casted.getDocumentInstance().getDocument().getWorkspace().getOwner()
              .getName(),
          casted.getDocumentInstance().getDocument().getWorkspace().getName(),
          casted.getDocumentInstance().getDocument().getName(),
          casted.getDocumentInstance().getVersion().toString(),
          casted.getNumber().toString() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new DocumentPageKey(values[0], values[1], values[2],
          Integer.valueOf(values[3]), Integer.valueOf(values[4]));
    }
  },

  DOCUMENT_VIEWER_ITEM(
      UNIQUE_SEGMENT_DOCUMENT_VIEWER_ITEMS,
      PATH_DOCUMENT_VIEWER_ITEM) {
    @Override
    protected String[] doGetValues(IKey key) {
      DocumentViewerItemKey casted = (DocumentViewerItemKey) key;
      return new String[] {
          casted.getDocumentInstance().getDocument().getWorkspace().getOwner()
              .getName(),
          casted.getDocumentInstance().getDocument().getWorkspace().getName(),
          casted.getDocumentInstance().getDocument().getName(),
          casted.getDocumentInstance().getVersion().toString(),
          casted.getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new DocumentViewerItemKey(values[0], values[1], values[2],
          Integer.valueOf(values[3]), values[4]);
    }
  },

  MANAGEMENT_WORKSPACE(
      UNIQUE_SEGMENT_MANAGEMENT_WORKSPACES,
      PATH_MANAGEMENT_WORKSPACE) {

    @Override
    protected String[] doGetValues(IKey key) {
      ManagementWorkspaceKey casted = (ManagementWorkspaceKey) key;
      return new String[] { casted.getOwner().getName(), casted.getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new ManagementWorkspaceKey(values[0], values[1]);
    }
  },

  MANAGEMENT_WORKSPACE_PERTICIPATION(
      EntityPaths.UNIQUE_SEGMENT_PATH_MANAGEMENT_WORKSPACES_PARTICIPATION,
      EntityPaths.PATH_MANAGEMENT_WORKSPACES_PARTICIPATION) {

    @Override
    protected String[] doGetValues(IKey key) {
      ManagementWorkspaceParticipationKey casted = (ManagementWorkspaceParticipationKey) key;
      return new String[] { casted.getPerticipant().getName(),
          casted.getManagementWorkspace().getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new ManagementWorkspaceParticipationKey(values[0], values[1],
          values[2]);
    }
  },

  SHARED_DOCUMENT(UNIQUE_SEGMENT_SHARED_DOCUMENTS, PATH_SHARED_DOCUMENT) {

    @Override
    protected String[] doGetValues(IKey key) {
      SharedDocumentKey casted = (SharedDocumentKey) key;
      return new String[] { casted.getWorkspace().getOwner().getName(),
          casted.getWorkspace().getName(),
          casted.getDocument().getWorkspace().getOwner().getName(),
          casted.getDocument().getWorkspace().getName(),
          casted.getDocument().getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new SharedDocumentKey(values[0], values[1], values[2], values[3],
          values[4]);
    }
  },

  INNETWORK_SHARED("INNETWORK_SHARED-broken", "INNETWORK_SHARED-broken") {

    @Override
    protected String[] doGetValues(IKey key) {
      SocialActorKey casted = (SocialActorKey) key;
      return new String[] { casted.getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new SocialActorKey(values[0]);
    }
  },

  DISPLAY_NOTICE("DISPLAY_NOTICE-broken", "DISPLAY_NOTICE-broken") {

    @Override
    protected String[] doGetValues(IKey key) {
      DisplayNoticeKey casted = (DisplayNoticeKey) key;
      return new String[] { String.valueOf(casted.getId()) };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new DisplayNoticeKey(values[0]);
    }
  },

  EXTERNAL_SHARED("EXTERNAL_SHARED-broken", "EXTERNAL_SHARED-broken") {

    @Override
    protected String[] doGetValues(IKey key) {
      SocialActorKey casted = (SocialActorKey) key;
      return new String[] { casted.getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new SocialActorKey(values[0]);
    }
  },

  EXTERNAL_SOCIAL_PERSON(
      "EXTERNAL_SOCIAL_PERSON-broken",
      "EXTERNAL_SOCIAL_PERSON-broken") {

    @Override
    protected String[] doGetValues(IKey key) {
      SocialActorKey casted = (SocialActorKey) key;
      return new String[] { casted.getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new SocialActorKey(values[0]);
    }
  },

  SOCIAL_ACTOR(UNIQUE_SEGMENT_ACTORS, PATH_ACTOR) {

    @Override
    protected String[] doGetValues(IKey key) {
      SocialActorKey casted = (SocialActorKey) key;
      return new String[] { casted.getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new SocialActorKey(values[0]);
    }
  },

  TASK(UNIQUE_SEGMENT_TASKS, PATH_TASK) {
    @Override
    protected String[] doGetValues(IKey key) {
      TaskKey casted = (TaskKey) key;
      return new String[] { casted.getWorkspace().getOwner().getName(),
          casted.getWorkspace().getName(), casted.getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new TaskKey(values[0], values[1], values[2]);
    }
  },

  COLLABORATION_WORKSPACE_INVITE(UNIQUE_SEGMENT_INVITES, PATH_INVITE) {
    @Override
    protected String[] doGetValues(IKey key) {
      InviteKey casted = (InviteKey) key;
      return new String[] { casted.getWorkspace().getOwner().getName(),
          casted.getWorkspace().getName(), casted.getInvitee().getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new InviteKey(values[0], values[1], values[2]);
    }
  },

  COLLABORATION_WORKSPACE_PARTICIPATION(
      UNIQUE_SEGMENT_PARTICIPATIONS,
      PATH_PARTICIPATION) {
    @Override
    protected String[] doGetValues(IKey key) {
      CollaborationWorkspaceParticipationKey casted = (CollaborationWorkspaceParticipationKey) key;
      return new String[] { casted.getWorkspace().getOwner().getName(),
          casted.getWorkspace().getName(), casted.getParticipant().getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new CollaborationWorkspaceParticipationKey(values[0], values[1],
          values[2]);
    }
  },

  WALL(UNIQUE_SEGMENT_WALLS, PATH_WALL) {
    @Override
    protected String[] doGetValues(IKey key) {
      return new String[] {};
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new WallKey<IWallContainerKey>(
          (IWallContainerKey) genericContainerKey);
    }

    @Override
    protected IKey doGetContainerKey(IKey key) {
      return ((WallKey<?>) key).getContainer();
    }
  },

  WALL_ENTRY(UNIQUE_SEGMENT_WALL_ENTRIES, PATH_WALL_ENTRY) {
    @Override
    protected String[] doGetValues(IKey key) {
      WallEntryKey<?> casted = (WallEntryKey<?>) key;
      return new String[] { casted.getNumber().toString() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new WallEntryKey<IWallContainerKey>(
          (IWallContainerKey) genericContainerKey, Integer.valueOf(values[0]));
    }

    @Override
    protected IKey doGetContainerKey(IKey key) {
      return ((WallEntryKey<?>) key).getWall().getContainer();
    }
  },

  FEED(UNIQUE_SEGMENT_FEEDS, PATH_FEED) {
    @Override
    protected String[] doGetValues(IKey key) {
      FeedKey casted = (FeedKey) key;
      return new String[] { casted.getOwner().getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new FeedKey(values[0]);
    }

    @Override
    protected IKey doGetContainerKey(IKey key) {
      return ((WallKey<?>) key).getContainer();
    }
  },

  FEED_ENTRY(UNIQUE_SEGMENT_FEED_ENTRIES, PATH_FEED_ENTRY) {
    @Override
    protected String[] doGetValues(IKey key) {
      FeedEntryKey casted = (FeedEntryKey) key;
      return new String[] { casted.getFeed().getOwner().getName(),
          casted.getNumber().toString() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new FeedEntryKey(values[0], Integer.valueOf(values[1]));
    }

  },

  CONNECTION(UNIQUE_SEGMENT_CONNECTIONS, PATH_CONNECTION) {
    @Override
    protected String[] doGetValues(IKey key) {
      ConnectionKey casted = (ConnectionKey) key;
      return new String[] { casted.getOwner().getName(),
          casted.getContact().getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new ConnectionKey(values[0], values[1]);
    }
  },

  PRODUCT(UNIQUE_SEGMENT_PRODUCTS, PATH_PRODUCT) {
    @Override
    protected String[] doGetValues(IKey key) {
      ProductKey casted = (ProductKey) key;
      return new String[] { casted.getOwner().getName(), casted.getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new ProductKey(values[0], values[1]);
    }
  },

  @Deprecated
  PRODUCT_TAG(UNIQUE_SEGMENT_PRODUCT_TAGS, PATH_PRODUCT_TAG) {
    @Override
    protected String[] doGetValues(IKey key) {
      ProductTagKey casted = (ProductTagKey) key;
      return new String[] { casted.getProduct().getOwner().getName(),
          casted.getProduct().getName(), casted.getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new ProductTagKey(values[0], values[1], values[2]);
    }
  },

  PRODUCT_FEATURE(UNIQUE_SEGMENT_PRODUCT_FEATURES, PATH_PRODUCT_FEATURE) {
    @Override
    protected String[] doGetValues(IKey key) {
      ProductFeatureKey casted = (ProductFeatureKey) key;
      return new String[] { casted.getProduct().getOwner().getName(),
          casted.getProduct().getName(), casted.getName() };
    }

    @Override
    protected IKey doGetKey(IKey genericContainerKey, String... values) {
      return new ProductFeatureKey(values[0], values[1], values[2]);
    }
  },
  ;

  private final String uniqueSegment;
  private final boolean generic;
  private final String[] patternSegments;
  private final boolean[] patternVariables;
  private final int variableCount;

  private EntityType(String uniqueSegment, String pattern) {
    this.uniqueSegment = uniqueSegment;
    generic = EntityPaths.isGeneric(pattern);
    String[] patternSegments = StringUtils.split(pattern, '/');
    if (generic) {
      checkGetContainerImplemented();
      patternSegments = ArrayUtils.subarray(patternSegments, 1,
          patternSegments.length);
    }
    this.patternSegments = patternSegments;
    patternVariables = new boolean[patternSegments.length];
    int variableCount = 0;
    for (int i = 0; i < patternSegments.length; i++) {
      String patternSegment = patternSegments[i];
      if (patternSegment.startsWith("{") && patternSegment.endsWith("}")) {
        patternVariables[i] = true;
        variableCount++;
      }
    }
    this.variableCount = variableCount;
  }

  private void checkGetContainerImplemented() {
    try {
      if (this.getClass().getDeclaredMethod("doGetContainerKey", IKey.class)
          .getDeclaringClass() != this.getClass()) {
        throw new NoSuchMethodError("EntityType " + name()
            + " is generic but does not implement doGetContainerKey(IKey)");
      }
    } catch (NoSuchMethodException e) {
      throw new NoSuchMethodError("EntityType " + name()
          + " is generic but does not implement doGetContainerKey(IKey)");
    }
  }

  public String getUniqueSegment() {
    return uniqueSegment;
  }

  protected abstract String[] doGetValues(IKey key);

  protected abstract IKey doGetKey(IKey genericContainerKey, String... values);

  protected IKey doGetContainerKey(IKey key) {
    return null;
  }

  public String getUri(IKey key) {
    String[] path = getPath(key);
    StringBuilder builder = new StringBuilder(path.length * 21);
    try {
      for (String segment : path) {
        builder.append('/');
        builder.append(EntityPaths.encodeSegment(segment));
      }
    } catch (URIException e) {
      throw new InvalidKeyException("Invalid key: "
          + StringUtils.join(path, '/'));
    }
    return builder.toString();

  }

  public String[] getPath(IKey key) {
    String[] values = doGetValues(key);
    String[] path;
    int offset;
    if (generic) {
      IKey containerKey = doGetContainerKey(key);
      String[] containerPath = containerKey.getEntityType().getPath(
          containerKey);
      path = new String[containerPath.length + patternSegments.length];
      System.arraycopy(containerPath, 0, path, 0, containerPath.length);
      offset = containerPath.length;
    } else {
      path = new String[patternSegments.length];
      offset = 0;
    }
    int variableIndex = 0;
    for (int i = 0; i < patternSegments.length; i++) {
      if (patternVariables[i]) {
        path[offset + i] = values[variableIndex++];
      } else {
        path[offset + i] = patternSegments[i];
      }
    }
    return path;
  }

  public static IKey getKey(String... path) {
    for (EntityType type : EntityType.values()) {
      try {
        IKey key = type.doGetKey(path);
        if (key != null) {
          return key;
        }
      } catch (IllegalArgumentException e) {
        throw new InvalidKeyException("Invalid key: "
            + StringUtils.join(path, '/'), e);
      }
    }
    throw new InvalidKeyException("Invalid key: " + StringUtils.join(path, '/'));
  }

  private IKey doGetKey(String[] path) {
    String[] values = match(path);
    if (values == null) {
      return null;
    }
    if (generic) {
      String[] containerPath = new String[path.length - patternSegments.length];
      System.arraycopy(path, 0, containerPath, 0, containerPath.length);
      IKey containerKey = getKey(containerPath);
      if (containerKey == null) {
        return null;
      }
      return doGetKey(containerKey, values);
    } else {
      return doGetKey(null, values);
    }
  }

  private String[] match(String[] path) {
    if (!generic && patternSegments.length != path.length || generic
        && patternSegments.length > path.length) {
      return null;
    }
    int offset = path.length - patternSegments.length;
    int variableIndex = 0;
    String[] values = new String[variableCount];
    for (int i = 0; i < patternSegments.length; i++) {
      String patternSegment = patternSegments[i];
      String pathSegment = path[offset + i];
      if (patternVariables[i]) {
        values[variableIndex++] = pathSegment;
      } else if (!patternSegment.equals(pathSegment)) {
        return null;
      }
    }
    return values;
  }

  public static EntityType valueOfUniqueSegment(String uniqueSegment) {
    for (EntityType type : EntityType.values()) {
      if (type.uniqueSegment.equals(uniqueSegment)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Invalid unique segment: "
        + uniqueSegment);
  }

  private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
  private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
      DATE_FORMAT_PATTERN);

  public static String formatTimestamp(Date value) {
    return DATE_FORMAT.format(value);
  }

  public static Date valueOfTimestamp(String value) {
    try {
      return DATE_FORMAT.parse(value);
    } catch (ParseException e) {
      throw new IllegalArgumentException("Invalid date for format "
          + DATE_FORMAT_PATTERN + ": " + value);
    }
  }
}
