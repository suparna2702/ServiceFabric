package com.similan.framework.dto.review;

import java.io.Serializable;
import java.util.List;

import com.similan.domain.entity.community.activity.CommentDetail;
import com.similan.domain.entity.community.activity.Reviewer;

public class ReviewCommentDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long reviewId;
	
	private Long reviewThreadId;
	
	private CommentDetail commentInfo;
	
	private Reviewer reviewer;
	
	private List<ReviewCommentDto> replyCommentInfo;
	
	public Long getReviewId() {
		return reviewId;
	}

	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}

	public Long getReviewThreadId() {
		return reviewThreadId;
	}

	public void setReviewThreadId(Long reviewThreadId) {
		this.reviewThreadId = reviewThreadId;
	}

	public CommentDetail getCommentInfo() {
		return commentInfo;
	}

	public void setCommentInfo(CommentDetail commentInfo) {
		this.commentInfo = commentInfo;
	}

	public Reviewer getReviewer() {
		return reviewer;
	}

	public void setReviewer(Reviewer reviewer) {
		this.reviewer = reviewer;
	}

	public List<ReviewCommentDto> getReplyCommentInfo() {
		return replyCommentInfo;
	}

	public void setReplyCommentInfo(List<ReviewCommentDto> replyCommentInfo) {
		this.replyCommentInfo = replyCommentInfo;
	}
}
