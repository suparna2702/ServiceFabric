package com.similan.domain.entity.community.activity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommentDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String comment;
	
	private Integer rating;
	
	private Long timeCommented;
	
	private Integer likeCount;
	
	private Integer dislikeCount;
	
	private Integer flagCount;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Long getTimeCommented() {
		return timeCommented;
	}

	public void setTimeCommented(Long timeCommented) {
		this.timeCommented = timeCommented;
	}

	public Integer getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}

	public Integer getDislikeCount() {
		return dislikeCount;
	}

	public void setDislikeCount(Integer dislikeCount) {
		this.dislikeCount = dislikeCount;
	}

	public Integer getFlagCount() {
		return flagCount;
	}
	
	public void setReviewDate(String dateFormatted){
		
	}
	
	public String getReviewDate(){
		
		Date dateCommented = new Date(this.timeCommented);
		DateFormat dateFormat = SimpleDateFormat.getDateInstance();
		return dateFormat.format(dateCommented);
	}

	public void setFlagCount(Integer flagCount) {
		this.flagCount = flagCount;
	}
}
