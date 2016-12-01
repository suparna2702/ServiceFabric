package com.similan.framework.dto.lead;

import java.util.Date;

public class CrmLeadActivityDto extends LeadActivityDto {

	private static final long serialVersionUID = 5946676830690324845L;

	private String crmId;
	
	private Date crmLastUpdateTimestamp;

	public String getCrmId() {
		return crmId;
	}

	public void setCrmId(String remoteId) {
		this.crmId = remoteId;
	}

	public Date getCrmLastUpdateTimestamp() {
		return crmLastUpdateTimestamp;
	}

	public void setCrmLastUpdateTimestamp(Date remoteLastUpdateTimestamp) {
		this.crmLastUpdateTimestamp = remoteLastUpdateTimestamp;
	}
	
}

