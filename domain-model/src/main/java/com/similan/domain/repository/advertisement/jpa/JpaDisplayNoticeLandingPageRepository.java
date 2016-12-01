package com.similan.domain.repository.advertisement.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.advertisement.DisplayNoticeLandingPage;

public interface JpaDisplayNoticeLandingPageRepository extends
    JpaRepository<DisplayNoticeLandingPage, Long> {

}
