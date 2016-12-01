package com.similan.framework.dto.review;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ReviewCommentSummeryDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer averageRating;
	
	private Map<String, Integer> ratingMap;
	
	private Long numOfReview;
	
	public ReviewCommentSummeryDto(){
		
		this.averageRating = 0;
		this.numOfReview = Long.valueOf("0");
		ratingMap = new HashMap<String, Integer>();
		
	}
	
	public Long getNumOfReview() {
		return numOfReview;
	}

	public void setNumOfReview(Long numOfReview) {
		this.numOfReview = numOfReview;
	}

	public Integer getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Integer averageRating) {
		this.averageRating = averageRating;
	}

	public Map<String, Integer> getRatingMap() {
		return ratingMap;
	}

	public void setRatingMap(Map<String, Integer> ratingMap) {
		this.ratingMap = ratingMap;
	}

	
}
