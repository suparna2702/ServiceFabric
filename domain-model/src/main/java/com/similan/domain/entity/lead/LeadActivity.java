package com.similan.domain.entity.lead;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name="LeadActivity")
public class LeadActivity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<LeadActivityComment> activityComments;
	
	@Column
	private Date dueDate;
	
	@Enumerated(EnumType.STRING)
	@Column
	private LeadActivityType leadActivityType;
	
	@Column
	private String subjectActivity;
	
	@Column(length=10000)
	private String activityDescription;
	
	@ManyToOne
	private Lead owner;
	
	@Column
	private Date timeStamp;
	
	@Column
	private Date startDate;

	@Column
	private Date endDate;
	
	@Column
	private String venue;
	

	public Lead getOwner() {
		return owner;
	}

	public void setOwner(Lead owner) {
		this.owner = owner;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<LeadActivityComment> getActivityComments() {
		return activityComments;
	}

	public void setActivityComments(List<LeadActivityComment> activityComments) {
		this.activityComments = activityComments;
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

	public String getSubjectActivity() {
		return subjectActivity;
	}

	public void setSubjectActivity(String subjectActivity) {
		this.subjectActivity = subjectActivity;
	}

	public String getActivityDescription() {
		return activityDescription;
	}

	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
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
