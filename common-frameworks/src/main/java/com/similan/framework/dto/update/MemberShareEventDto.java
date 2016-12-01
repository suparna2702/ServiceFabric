package com.similan.framework.dto.update;

import com.similan.domain.entity.community.PhotoViewOption;

public class MemberShareEventDto extends WallEventDto {

	private static final long serialVersionUID = -2180073120040174475L;

	private Long memberId;
	private String memberName;
	private String memberPhotoLocation;
	private String comment;

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberPhotoLocation() {
		return PhotoViewOption.ShowPhoto
				   .effectivePhoto("/images/memberDefaultPhoto.png", this.memberPhotoLocation);
	}

	public void setMemberPhotoLocation(String memberPhotoLocation) {
		this.memberPhotoLocation = memberPhotoLocation;
	}

	@Override
	public String toString() {
		return "MemberProfileChangedEventDto [memberName=" + memberName
				+ ", memberPhotoLocation=" + memberPhotoLocation + "]";
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
}
