package com.similan.domain.repository.community.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.activity.ReviewTag;
import com.similan.domain.entity.community.activity.ReviewTagType;

public interface JpaReviewTagRepository extends JpaRepository<ReviewTag, Long> {
	
	@Query("select count(reviewTag) from ReviewTag reviewTag where reviewTag.reviewTagType=:tagType and reviewTag.taggedItemId=:taggedItemId")
	public Long getCountByTagType(@Param("tagType")ReviewTagType tagType, @Param("taggedItemId")Long taggedItemId);
	
	@Query("select reviewTag from ReviewTag reviewTag where reviewTag.reviewTagType=:tagType and reviewTag.taggedItemId=:tagId")
	public List<ReviewTag> getReviewTagsByTagTypeAndTagId(
			@Param("tagType")ReviewTagType tagType, @Param("tagId")Long tagId);

}
