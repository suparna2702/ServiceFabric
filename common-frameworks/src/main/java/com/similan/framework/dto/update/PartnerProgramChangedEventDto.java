package com.similan.framework.dto.update;

import com.similan.domain.entity.community.PhotoViewOption;

public class PartnerProgramChangedEventDto extends WallEventDto {

	private static final long serialVersionUID = -2180073120040174475L;

	private Long partnerProgramId;
	private String partnerProgramName;
	private String partnerProgramLogoLocation;
	private String partnerProgramOwnerName;
	
	public Long getPartnerProgramId() {
		return partnerProgramId;
	}
	public void setPartnerProgramId(Long partnerProgramId) {
		this.partnerProgramId = partnerProgramId;
	}
	public String getPartnerProgramName() {
		return partnerProgramName;
	}
	public void setPartnerProgramName(String partnerProgramName) {
		this.partnerProgramName = partnerProgramName;
	}
	public String getPartnerProgramLogoLocation() {
		return PhotoViewOption.ShowPhoto
				   .effectivePhoto("/images/businessLogo.jpg", this.partnerProgramLogoLocation);
	}
	public void setPartnerProgramLogoLocation(String partnerProgramLogoLocation) {
		this.partnerProgramLogoLocation = partnerProgramLogoLocation;
	}
	public String getPartnerProgramOwnerName() {
		return partnerProgramOwnerName;
	}
	public void setPartnerProgramOwnerName(String partnerProgramOwnerName) {
		this.partnerProgramOwnerName = partnerProgramOwnerName;
	}
	@Override
	public String toString() {
		return "PartnerProgramChangedEventDto [partnerProgramId="
				+ partnerProgramId + ", partnerProgramName="
				+ partnerProgramName + ", partnerProgramLogoLocation="
				+ partnerProgramLogoLocation + ", partnerProgramOwnerName="
				+ partnerProgramOwnerName + "]";
	}
	
}
