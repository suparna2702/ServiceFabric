package com.similan.domain.repository.document.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.DocumentLabel;

public interface JpaDocumentLabelRepository extends
    JpaRepository<DocumentLabel, Long> {

  @Query("select label from DocumentLabel label"
      + " where label.owner.name = :ownerName" + " and label.name = :name")
  DocumentLabel findOne(@Param("ownerName") String ownerName,
      @Param("name") String name);

  @Query("select label from DocumentLabel label"
      + " where label.owner = :owner" + " and label.parent = :parent"
      + " order by label.name")
  List<DocumentLabel> findByOwnerAndParent(
      @Param("owner") SocialActor owner,
      @Param("parent") DocumentLabel parent);
  
  @Query("select label from DocumentLabel label"
	      + " where label.owner = :owner" + " and label.parent is null"
	      + " order by label.name")
  List<DocumentLabel> findByOwnerAndNullParent(
	      @Param("owner") SocialActor owner);

}
