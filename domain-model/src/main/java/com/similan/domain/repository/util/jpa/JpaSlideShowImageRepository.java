package com.similan.domain.repository.util.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.util.SlideShowImage;

public interface JpaSlideShowImageRepository 
              extends JpaRepository<SlideShowImage, Long> {

}
