package com.similan.domain.repository.community.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialContactGroup;

public interface JpaSocialContactGroupRepository extends JpaRepository<SocialContactGroup, Long> {
	
	List<SocialContactGroup> findByOwner(SocialActor owner);

}
