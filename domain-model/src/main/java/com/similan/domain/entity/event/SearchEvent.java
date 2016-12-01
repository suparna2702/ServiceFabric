package com.similan.domain.entity.event;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "SearchEvent")
@DiscriminatorValue("SearchEvent")
public class SearchEvent extends CommunityEvent {
	
	@Column(length=10000)
	private String fileUUID;
	
	public String getFileUUID() {
		return fileUUID;
	}

	public void setFileUUID(String fileUUID) {
		this.fileUUID = fileUUID;
	}
}
