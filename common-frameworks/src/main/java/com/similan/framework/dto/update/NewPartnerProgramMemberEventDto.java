package com.similan.framework.dto.update;

public class NewPartnerProgramMemberEventDto extends WallEventDto {

	private static final long serialVersionUID = -2180073120040174475L;

	private Long partnershipId;
	private String partnerProgramName;
	private String partnerProgramLogoLocation;
	private String partnerLogoLocation;
	private String partnerName;
	
	public Long getPartnershipId() {
		return partnershipId;
	}
	public void setPartnershipId(Long partnershipId) {
		this.partnershipId = partnershipId;
	}
	public String getPartnerProgramName() {
		return partnerProgramName;
	}
	public void setPartnerProgramName(String partnerProgramName) {
		this.partnerProgramName = partnerProgramName;
	}
	public String getPartnerProgramLogoLocation() {
		return partnerProgramLogoLocation;
	}
	public void setPartnerProgramLogoLocation(String partnerProgramLogoLocation) {
		this.partnerProgramLogoLocation = partnerProgramLogoLocation;
	}
	public String getPartnerLogoLocation() {
		return partnerLogoLocation;
	}
	public void setPartnerLogoLocation(String partnerLogoLocation) {
		this.partnerLogoLocation = partnerLogoLocation;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	@Override
	public String toString() {
		return "NewPartnerProgramMemberEventDto [partnershipId="
				+ partnershipId + ", partnerProgramName=" + partnerProgramName
				+ ", partnerProgramLogoLocation=" + partnerProgramLogoLocation
				+ ", partnerLogoLocation=" + partnerLogoLocation
				+ ", partnerName=" + partnerName + "]";
	}

}
