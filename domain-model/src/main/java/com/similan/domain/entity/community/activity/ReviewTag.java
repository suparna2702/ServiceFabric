package com.similan.domain.entity.community.activity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "ReviewTag")
public class ReviewTag {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column
	private ReviewTagType reviewTagType;
	
	@Column
	private Long taggerId;
	
	@Column
	private Long taggedItemId;
	
	@Column
	@Enumerated(EnumType.STRING)
	private ReviewTagItemType taggedItemType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getTaggedItemId() {
		return taggedItemId;
	}

	public void setTaggedItemId(Long taggedItemId) {
		this.taggedItemId = taggedItemId;
	}

	public ReviewTagItemType getTaggedItemType() {
		return taggedItemType;
	}

	public void setTaggedItemType(ReviewTagItemType taggedItemType) {
		this.taggedItemType = taggedItemType;
	}

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
}
