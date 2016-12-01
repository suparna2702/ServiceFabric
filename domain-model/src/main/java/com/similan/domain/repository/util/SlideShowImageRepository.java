package com.similan.domain.repository.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.util.SlideShowImage;
import com.similan.domain.repository.util.jpa.JpaSlideShowImageRepository;

@Repository
public class SlideShowImageRepository {
  @Autowired
  private JpaSlideShowImageRepository repository;
	
	public SlideShowImage save(SlideShowImage image) {
    return repository.save(image);
  }
	
	public List<SlideShowImage> findAll() {
    return repository.findAll();
  }
	
	public SlideShowImage create(){
		SlideShowImage image = new SlideShowImage();
		return image;
	}

}
