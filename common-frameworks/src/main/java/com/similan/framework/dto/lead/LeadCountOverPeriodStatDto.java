package com.similan.framework.dto.lead;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LeadCountOverPeriodStatDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long leadCount;
	
	private long periodTo;

	public Long getLeadCount() {
		return leadCount;
	}

	public void setLeadCount(Long leadCount) {
		this.leadCount = leadCount;
	}

	public long getPeriodTo() {
		return periodTo;
	}

	public void setPeriodTo(long periodTo) {
		this.periodTo = periodTo;
	}
	
	public String getFormattedDate(){
		Date dateCommented = new Date(this.periodTo);
		DateFormat dateFormat = SimpleDateFormat.getDateInstance();
		return dateFormat.format(dateCommented);
	}
}
