package com.similan.portal.model;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.List;

import com.similan.service.api.comment.dto.extended.CommentAndRepliesDto;
import com.similan.service.api.document.dto.basic.DocumentPageDto;
import com.similan.service.api.document.dto.key.DocumentPageKey;

public class DocumentPageModel implements Serializable {

	private static final long serialVersionUID = -1613814314887775015L;
	private DocumentPageDto data;
	private List<CommentAndRepliesDto<DocumentPageKey>> comments;
	private String pageStorageKey;

	public DocumentPageModel(DocumentPageDto data) {
		this.data = data;
	}

	public String getName() {
		return data.getKey().getNumber().toString();
	}

	public int hashCode() {
		return data.hashCode();
	}

	public boolean equals(Object other) {
		return data.equals(other);
	}

	public DocumentPageDto getData() {
		return data;
	}

	public List<CommentAndRepliesDto<DocumentPageKey>> getComments() {
		return comments;
	}

	public void setComments(List<CommentAndRepliesDto<DocumentPageKey>> comments) {
		this.comments = comments;
	}

	public int getCommentCount() {
		if (getComments() != null)
				return getComments().size();
		return 0;
	}

	public String getPageStorageKey() {
		// {workspaceOwnerName}/{workspaceName}/{documentName}/{documentInstanceVersion}/{number}",
		// method = RequestMethod.GET
		if (pageStorageKey == null) {
			try {
				StringBuilder builder = new StringBuilder();
				builder.append(
						data.getKey().getDocumentInstance().getDocument()
								.getWorkspace().getOwner().getName())
						.append('/')
						.append(URLEncoder.encode(data.getKey()
								.getDocumentInstance().getDocument()
								.getWorkspace().getName(), "ISO-8859-1"))
						.append('/')
						.append(URLEncoder.encode(data.getKey()
								.getDocumentInstance().getDocument().getName(),
								"ISO-8859-1"))
						.append('/')
						.append(data.getKey().getDocumentInstance()
								.getVersion()).append('/')
						.append(data.getKey().getNumber());

				pageStorageKey = builder.toString();
			} catch (Exception e) {
				pageStorageKey = "";
			}
		}
		return pageStorageKey;
	}
}
