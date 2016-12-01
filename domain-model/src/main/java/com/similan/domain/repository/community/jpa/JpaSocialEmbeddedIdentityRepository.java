package com.similan.domain.repository.community.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialEmbeddedIdentity;

public interface JpaSocialEmbeddedIdentityRepository extends
                                             JpaRepository<SocialEmbeddedIdentity, Long>{
	
	@Query("select emIdentity from EmbeddedIdentity emIdentity where emIdentity.uuid=:UUID")
	public SocialEmbeddedIdentity findEmbeddedIdentity(@Param("UUID")String UUID);
	
	@Query("select emIdentity from EmbeddedIdentity emIdentity where emIdentity.embeddedActor=:actorIdentity")
	public SocialEmbeddedIdentity findEmbeddedIdentity(@Param("actorIdentity")SocialActor actorIdentity);

}
