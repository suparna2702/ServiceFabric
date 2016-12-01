package com.similan.framework.dto.partner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PartnerProgramResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private Date timeStamp;
	
	private List<PartnerPreQualificationAnswerDto> preQualQuestionResponses;
	
	public PartnerProgramResponseDto(){
		id = Long.MIN_VALUE;
		preQualQuestionResponses = new ArrayList<PartnerPreQualificationAnswerDto>();
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public List<PartnerPreQualificationAnswerDto> getPreQualQuestionResponses() {
		return preQualQuestionResponses;
	}

	public void setPreQualQuestionResponses(
			List<PartnerPreQualificationAnswerDto> preQualQuestionResponses) {
		this.preQualQuestionResponses = preQualQuestionResponses;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	
}
