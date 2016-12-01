package com.similan.domain.repository.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.activity.ReviewTag;
import com.similan.domain.entity.community.activity.ReviewTagType;
import com.similan.domain.repository.community.jpa.JpaReviewTagRepository;

@Repository
public class ReviewTagRepository {
  @Autowired
  private JpaReviewTagRepository repository;
	
	public ReviewTag save(ReviewTag reviewTag) {
    return repository.save(reviewTag);
  }
	
	public List<ReviewTag> findAll() {
    return repository.findAll();
  }
	
	public Long getCountByTagType(ReviewTagType tagType, Long taggedItemId) {
    return repository.getCountByTagType(tagType, taggedItemId);
  }
	
	public List<ReviewTag> getReviewTagsByTagTypeAndTagId(
			ReviewTagType dislike, Long newsId) {
    return repository.getReviewTagsByTagTypeAndTagId(
			dislike, newsId);
  }
	
	public ReviewTag createReviewTag(){
		ReviewTag reviewTag = new ReviewTag();
		return reviewTag;
	}
}
