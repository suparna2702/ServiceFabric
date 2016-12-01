package com.similan.service.api.base.dto.key;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;

public class EntityPaths {
  @Deprecated
  public static final String SEGMENT_PRODUCT_CATEGORIES = "product-categories";
  @Deprecated
  public static final String SEGMENT_PRODUCT_TAGS = "product-tags";

  public static final String PATH_POLL_TEMPLATE = "actors/{owner.name}/polltemplate/{name}";
  public static final String PATH_TAG = "tag/{id}";
  public static final String PATH_TAG_LINK = "taglink/{id}";
  public static final String PATH_AUDIT_EVENT = "auditevent/{id}";
  public static final String PATH_BOOKMARK = "bookmarks/{id}";
  public static final String PATH_CHAT_SESSION = "actors/{initiator.name}/chats/{firstInvitee.name}/{date}";
  public static final String PATH_COLLABORATION_WORKSPACES = "actors/{owner.name}/collabspaces";
  public static final String PATH_COLLABORATION_WORKSPACE = PATH_COLLABORATION_WORKSPACES
      + "/{name}";
  public static final String PATH_COMMENTS = "{commentablePath:.*}/comments";
  public static final String PATH_COMMENT = PATH_COMMENTS + "/{number}";
  public static final String PATH_COMMENT_REPLIES = "{commentablePath:.*}/comments/{comment.number}/replies";
  public static final String PATH_COMMENT_REPLY = PATH_COMMENT_REPLIES
      + "/{number}";
  public static final String PATH_CONFERENCES = "actors/{workspace.owner.name}/collabspaces/{workspace.name}/conferences";
  public static final String PATH_CONFERENCE = PATH_CONFERENCES + "/{name}";
  public static final String PATH_DOCUMENTS = "actors/{workspace.owner.name}/workspaces/{workspace.name}/documents";
  public static final String PATH_DOCUMENT = PATH_DOCUMENTS + "/{name}";
  public static final String PATH_CATEGORIES = "actors/{owner.name}/categories";
  public static final String PATH_CATEGORY = PATH_CATEGORIES + "/{name}";
  public static final String PATH_DOCUMENT_INSTANCES = "actors/{document.workspace.owner.name}/workspaces/{document.workspace.name}/documents/{document.name}/versions";
  public static final String PATH_DOCUMENT_INSTANCE = PATH_DOCUMENT_INSTANCES
      + "/{number}";
  public static final String PATH_LABELS = "actors/{owner.name}/labels";
  public static final String PATH_LABEL = PATH_LABELS + "/{name}";
  public static final String PATH_DOCUMENT_PAGES = "actors/{documentInstance.document.workspace.owner.name}/workspaces/{documentInstance.document.workspace.name}/documents/{documentInstance.document.name}/versions/{documentInstance.number}/pages";
  public static final String PATH_DOCUMENT_PAGE = PATH_DOCUMENT_PAGES
      + "/{number}";
  public static final String PATH_DOCUMENT_VIEWER_ITEMS = "actors/{documentInstance.document.workspace.owner.name}/workspaces/{documentInstance.document.workspace.name}/documents/{documentInstance.document.name}/versions/{documentInstance.number}/viewer-items";
  public static final String PATH_DOCUMENT_VIEWER_ITEM = PATH_DOCUMENT_VIEWER_ITEMS
      + "/{number}";
  public static final String PATH_MANAGEMENT_WORKSPACES = "actors/{owner.name}/workspaces";
  public static final String PATH_MANAGEMENT_WORKSPACE = PATH_MANAGEMENT_WORKSPACES
      + "/{name}";
  public static final String PATH_MANAGEMENT_WORKSPACES_PARTICIPATIONS = "actors/{workspace.owner.name}/workspaces/{workspace.name}/participants";
  public static final String PATH_MANAGEMENT_WORKSPACES_PARTICIPATION = PATH_MANAGEMENT_WORKSPACES_PARTICIPATIONS
      + "/{participant.name}";
  public static final String PATH_SHARED_DOCUMENTS = "actors/{workspace.owner.name}/collabspaces/{workspace.name}/documents";
  public static final String PATH_SHARED_DOCUMENT = PATH_SHARED_DOCUMENTS
      + "/{document.workspace.owner.name}/{document.workspace.name}/{document.name}";
  public static final String PATH_ACTORS = "actors";
  public static final String PATH_ACTOR = PATH_ACTORS + "/{name}";
  public static final String PATH_SURVEYS = "actors/{workspace.owner.name}/collabspaces/{workspace.name}/survey";
  public static final String PATH_SURVEY = PATH_SURVEYS + " /{name}";
  public static final String PATH_TASKS = "actors/{workspace.owner.name}/collabspaces/{workspace.name}/tasks/{name}";
  public static final String PATH_TASK = PATH_TASKS + "/{name}";
  public static final String PATH_INVITES = "actors/{workspace.owner.name}/collabspaces/{workspace.name}/invites";
  public static final String PATH_INVITE = PATH_INVITES + "/{invitee.name}";
  public static final String PATH_PARTICIPATIONS = "actors/{workspace.owner.name}/collabspaces/{workspace.name}/participants";
  public static final String PATH_PARTICIPATION = PATH_PARTICIPATIONS
      + "/{participant.name}";
  public static final String PATH_WALL = "{wallContainerPath:.*}/wall";
  public static final String PATH_WALL_ENTRIES = "{wallContainerPath:.*}/wall/entries";
  public static final String PATH_WALL_ENTRY = PATH_WALL_ENTRIES + "/{number}";
  public static final String PATH_FEED = "{owner.name}/feed";
  public static final String PATH_FEED_ENTRIES = "actors/{feed.owner.name}/feed/entries";
  public static final String PATH_FEED_ENTRY = PATH_FEED_ENTRIES + "/{number}";
  public static final String PATH_CONNECTIONS = "{owner.name}/contacts";
  public static final String PATH_CONNECTION = PATH_CONNECTIONS
      + "{contact.name}";
  public static final String PATH_PRODUCTS = "actors/{owner.name}/products";
  public static final String PATH_PRODUCT = PATH_PRODUCTS + "/{name}";
  public static final String PATH_PRODUCT_FEATURES = "actors/{product.owner.name}/product/{product.name}/features";
  public static final String PATH_PRODUCT_FEATURE = PATH_PRODUCT_FEATURES
      + "/{name}";
  public static final String PATH_PARTNER_PROGRAMS = "actors/{owner.name}/partner-programs";
  public static final String PATH_PARTNER_PROGRAM = PATH_PARTNER_PROGRAMS
      + "/{name}";

  @Deprecated
  public static final String PATH_PRODUCT_TAGS = "{product.owner.name}/product/{product.name}/tags";
  @Deprecated
  public static final String PATH_PRODUCT_TAG = PATH_PRODUCT_TAGS + "/{name}";

  public static final String UNIQUE_SEGMENT_TAG = "tag";
  public static final String UNIQUE_SEGMENT_TAG_LINK = "taglink";
  public static final String UNIQUE_SEGMENT_AUDIT_EVENT = "auditevent";
  public static final String UNIQUE_SEGMENT_POLL_TEMPLATE = "polltemplate";
  public static final String UNIQUE_SEGMENT_BOOKMARKS = "bookmarks";
  public static final String UNIQUE_SEGMENT_CHAT_SESSIONS = "chats";
  public static final String UNIQUE_SEGMENT_COLLABORATION_WORKSPACES = "collabspaces";
  public static final String UNIQUE_SEGMENT_COMMENTS = "comments";
  public static final String UNIQUE_SEGMENT_COMMENT_REPLIES = "comment-replies";
  public static final String UNIQUE_SEGMENT_CONFERENCES = "conferences";
  public static final String UNIQUE_SEGMENT_DOCUMENTS = "documents";
  public static final String UNIQUE_SEGMENT_CATEGORIES = "categories";
  public static final String UNIQUE_SEGMENT_DOCUMENT_INSTANCES = "document-instances";
  public static final String UNIQUE_SEGMENT_LABELS = "labels";
  public static final String UNIQUE_SEGMENT_DOCUMENT_PAGES = "pages";
  public static final String UNIQUE_SEGMENT_DOCUMENT_VIEWER_ITEMS = "document-viewer-items";
  public static final String UNIQUE_SEGMENT_MANAGEMENT_WORKSPACES = "workspaces";
  public static final String UNIQUE_SEGMENT_SHARED_DOCUMENTS = "shared-documents";
  public static final String UNIQUE_SEGMENT_PATH_MANAGEMENT_WORKSPACES_PARTICIPATION = "managementspace-perticipation";
  public static final String UNIQUE_SEGMENT_ACTORS = "actors";
  public static final String UNIQUE_SEGMENT_SURVEYS = "survey";
  public static final String UNIQUE_SEGMENT_TASKS = "tasks";
  public static final String UNIQUE_SEGMENT_INVITES = "invites";
  public static final String UNIQUE_SEGMENT_PARTICIPATIONS = "participants";
  public static final String UNIQUE_SEGMENT_WALLS = "walls";
  public static final String UNIQUE_SEGMENT_WALL_ENTRIES = "wall-entries";
  public static final String UNIQUE_SEGMENT_FEEDS = "feeds";
  public static final String UNIQUE_SEGMENT_FEED_ENTRIES = "feed-entries";
  public static final String UNIQUE_SEGMENT_CONNECTIONS = "contacts";
  public static final String UNIQUE_SEGMENT_PRODUCTS = "products";
  public static final String UNIQUE_SEGMENT_PRODUCT_FEATURES = "product-features";
  public static final String UNIQUE_SEGMENT_PARTNER_PROGRAM = "partner-program";

  @Deprecated
  public static final String UNIQUE_SEGMENT_PRODUCT_TAGS = "product-tags";

  public static boolean isGeneric(String path) {
    int end = path.indexOf('/');
    if (end == -1) {
      end = path.length();
    }
    boolean generic = path.startsWith("{")
        && path.substring(0, end).endsWith(":.*}");
    return generic;
  }

  public static String encodeSegment(String segment) throws URIException {
    return URIUtil.encodeWithinPath(segment, "UTF-8");
  }

}
