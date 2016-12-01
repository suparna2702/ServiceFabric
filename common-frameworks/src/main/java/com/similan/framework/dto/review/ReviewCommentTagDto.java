package com.similan.framework.dto.review;

import java.io.Serializable;

import com.similan.domain.entity.community.activity.ReviewTagType;

public class ReviewCommentTagDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ReviewTagType reviewTagType;
	
	private Long taggerId;
	
	private Long reviewCommentId;

	public ReviewTagType getReviewTagType() {
		return reviewTagType;
	}

	public void setReviewTagType(ReviewTagType reviewTagType) {
		this.reviewTagType = reviewTagType;
	}

	public Long getTaggerId() {
		return taggerId;
	}

	public void setTaggerId(Long taggerId) {
		this.taggerId = taggerId;
	}

	public Long getReviewCommentId() {
		return reviewCommentId;
	}

	public void setReviewCommentId(Long reviewCommentId) {
		this.reviewCommentId = reviewCommentId;
	}
}
