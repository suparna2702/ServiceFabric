package com.similan.domain.entity.community.activity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "Review")
public class Review {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Embedded
	private Reviewer reviewer;
	
	@Embedded
    private CommentDetail commentDetail;
	
	@Column
	private Long reviewThreadId;
	
	public Long getReviewThreadId() {
		return reviewThreadId;
	}

	public void setReviewThreadId(Long reviewThreadId) {
		this.reviewThreadId = reviewThreadId;
	}

	public Reviewer getReviewer() {
		return reviewer;
	}

	public void setReviewer(Reviewer reviewer) {
		this.reviewer = reviewer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CommentDetail getCommentDetail() {
		return commentDetail;
	}

	public void setCommentDetail(CommentDetail commentDetail) {
		this.commentDetail = commentDetail;
	}
}
