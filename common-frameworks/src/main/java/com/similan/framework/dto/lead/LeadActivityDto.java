package com.similan.framework.dto.lead;

import java.io.Serializable;
import java.util.Date;

import com.similan.domain.entity.lead.LeadActivityType;

public class LeadActivityDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String subjectActivity;
	
	private Date dueDate;
	
	private LeadActivityType leadActivityType = LeadActivityType.Phone;
	
	private String leadActivityPriority;
	
	private String activityDescription;
	
	private Date timeStamp;
	
	private Date startDate;
	
	private Date endDate;
	 
	private String venue;
	
	public String getActivityIcon(){
		
		String retIcon = "/images/lead/defaultActivityIcon.png";
		
		if(leadActivityType.equals(LeadActivityType.Phone) == true){
			retIcon = "/images/lead/phoneActivityIcon.png";
		}
		else if(leadActivityType.equals(LeadActivityType.Meeting) == true) {
			retIcon = "/images/lead/meetActivityIcon.png";
		}
		
		return retIcon;
	}
	
	public void setActivityIcon(String actIcon){
		/* dont do anything */
	}

	public String getSubjectActivity() {
		return subjectActivity;
	}

	public void setSubjectActivity(String subjectActivity) {
		this.subjectActivity = subjectActivity;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	public LeadActivityType getLeadActivityType() {
		return leadActivityType;
	}

	public void setLeadActivityType(LeadActivityType leadActivityType) {
		this.leadActivityType = leadActivityType;
	}

	public String getLeadActivityPriority() {
		return leadActivityPriority;
	}

	public void setLeadActivityPriority(String leadActivityPriority) {
		this.leadActivityPriority = leadActivityPriority;
	}

	public String getActivityDescription() {
		return activityDescription;
	}

	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}

	@Override
	public String toString() {
		return "LeadActivityDto [subjectActivity=" + subjectActivity
				+ ", dueDate=" + dueDate + ", leadActivityType="
				+ leadActivityType + ", leadActivityPriority="
				+ leadActivityPriority + ", activityDescription="
				+ activityDescription + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}
	
	
	
}
