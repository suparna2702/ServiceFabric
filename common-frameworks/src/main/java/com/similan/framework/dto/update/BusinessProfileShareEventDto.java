package com.similan.framework.dto.update;


public class BusinessProfileShareEventDto extends WallEventDto {

	private static final long serialVersionUID = -2180073120040174475L;

	private Long memberId;
	private String memberName;
	private String memberPhotoLocation;
	private Long organizationId;
	private String organizationName;

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	@Override
	public String toString() {
		return "BusinessProfileChangedEventDto [organizationName="
				+ organizationName + ", organizationLogoLocation=" + "]";
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberPhotoLocation() {
		return memberPhotoLocation;
	}

	public void setMemberPhotoLocation(String memberPhotoLocation) {
		this.memberPhotoLocation = memberPhotoLocation;
	}

}
