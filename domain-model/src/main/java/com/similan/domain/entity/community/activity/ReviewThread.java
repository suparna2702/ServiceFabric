package com.similan.domain.entity.community.activity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "ReviewThread")
public class ReviewThread {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
	private Long socialActorId;
	
	@OneToMany
	private List<Review> reviews = new ArrayList<Review>();

	public Long getSocialActorId() {
		return socialActorId;
	}

	public void setSocialActorId(Long socialActorId) {
		this.socialActorId = socialActorId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
}
