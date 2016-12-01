package com.similan.framework.dto.update;

import com.similan.domain.entity.community.PhotoViewOption;

public class BusinessShareEventDto extends WallEventDto {

	private static final long serialVersionUID = -2180073120040174475L;

	private Long organizationId;
	private String organizationName;
	private String organizationLogoLocation;
	private String comment;
	
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getOrganizationLogoLocation() {
		return PhotoViewOption.ShowPhoto
		   .effectivePhoto("/images/businessLogo.jpg", this.organizationLogoLocation);
	}
	
	public void setOrganizationLogoLocation(String organizationLogoLocation) {
		this.organizationLogoLocation = organizationLogoLocation;
	}
	@Override
	public String toString() {
		return "BusinessProfileChangedEventDto [organizationName="
				+ organizationName + ", organizationLogoLocation="
				+ organizationLogoLocation + "]";
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	
}
