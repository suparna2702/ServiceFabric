package com.similan.domain.repository.document.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.DocumentCategory;

public interface JpaDocumentCategoryRepository extends
    JpaRepository<DocumentCategory, Long> {

  @Query("select category from DocumentCategory category"
      + " where category.owner.name = :ownerName"
      + " and category.name = :name")
  DocumentCategory findOne(@Param("ownerName") String ownerName,
      @Param("name") String name);

  @Query("select category from DocumentCategory category"
      + " where category.owner = :owner" + " order by name")
  List<DocumentCategory> findByOwner(@Param("owner") SocialActor owner);

}
