package com.similan.framework.dto.lead;

import java.util.Date;

public class CrmLeadDto extends LeadDto  {


	private static final long serialVersionUID = -2835646054010389040L;

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
