package com.similan.domain.entity.community.activity;

import java.io.Serializable;

public class Reviewer implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private Long reviewerId;
    
	private String reviewerIcon;
	
	private String reviewerName;
	
	private String reviewerDescription;

	public Long getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(Long reviewerId) {
		this.reviewerId = reviewerId;
	}

	public String getReviewerIcon() {
		return reviewerIcon;
	}

	public void setReviewerIcon(String reviewerIcon) {
		this.reviewerIcon = reviewerIcon;
	}

	public String getReviewerName() {
		return reviewerName;
	}

	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}

	public String getReviewerDescription() {
		return reviewerDescription;
	}

	public void setReviewerDescription(String reviewerDescription) {
		this.reviewerDescription = reviewerDescription;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
