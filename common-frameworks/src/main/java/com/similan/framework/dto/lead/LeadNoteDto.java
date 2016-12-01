package com.similan.framework.dto.lead;

import java.io.Serializable;
import java.util.UUID;

import com.similan.domain.entity.community.PhotoViewOption;
import com.similan.domain.entity.lead.LeadNote;

public class LeadNoteDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private LeadNote leadNote = new LeadNote();
	
	private String commenterFirstName;
	
	private String commenterLastName;
	
	private String commenterPhoto;
	
	private Long commenterId;
	
	private String uuId;
	
	private boolean childNote=false;
	
	public LeadNoteDto(){
		id = Long.MIN_VALUE;
		uuId = UUID.randomUUID().toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUuId() {
		return uuId;
	}

	public void setUuId(String uuId) {
		this.uuId = uuId;
	}

	public Long getCommenterId() {
		return commenterId;
	}

	public void setCommenterId(Long commenterId) {
		this.commenterId = commenterId;
	}

	public String getCommenterFirstName() {
		return commenterFirstName;
	}

	public void setCommenterFirstName(String commenterFirstName) {
		this.commenterFirstName = commenterFirstName;
	}

	public String getCommenterLastName() {
		return commenterLastName;
	}

	public void setCommenterLastName(String commenterLastName) {
		this.commenterLastName = commenterLastName;
	}

	public String getCommenterPhoto() {
		return PhotoViewOption.ShowPhoto
				        .effectivePhoto("/images/memberDefaultPhoto.png", this.commenterPhoto);
	}

	public void setCommenterPhoto(String commenterPhoto) {
		this.commenterPhoto = commenterPhoto;
	}

	public LeadNote getLeadNote() {
		return leadNote;
	}

	public void setLeadNote(LeadNote leadNote) {
		this.leadNote = leadNote;
	}

	@Override
	public String toString() {
		return "LeadNoteDto [id=" + id + ", leadNote=" + leadNote
				+ ", commenterFirstName=" + commenterFirstName
				+ ", commenterLastName=" + commenterLastName
				+ ", commenterPhoto=" + commenterPhoto + ", commenterId="
				+ commenterId + ", uuId=" + uuId + "]";
	}

	public boolean isChildNote() {
		return childNote;
	}

	public void setChildNote(boolean childNote) {
		this.childNote = childNote;
	}
	
	

}
