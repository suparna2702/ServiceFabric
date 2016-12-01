package com.similan.domain.repository.wall;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.wall.wall.LinkReference;

public interface LinkReferenceRepository extends
    JpaRepository<LinkReference, Long> {
}
