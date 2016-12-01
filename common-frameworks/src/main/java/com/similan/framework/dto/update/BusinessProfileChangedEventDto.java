package com.similan.framework.dto.update;

import com.similan.domain.entity.community.PhotoViewOption;

public class BusinessProfileChangedEventDto extends WallEventDto {

	private static final long serialVersionUID = -2180073120040174475L;

	private String organizationName;
	private String organizationLogoLocation;
	private Long organizationId;
	
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
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	
}
