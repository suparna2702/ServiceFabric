package com.similan.domain.repository.community.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.SocialActor;

public interface JpaSocialActorRepository extends
    JpaRepository<SocialActor, Long> {

  @Query("select actor from SocialActor actor where actor.name = :name")
  public abstract SocialActor findOne(@Param("name") String name);

}
