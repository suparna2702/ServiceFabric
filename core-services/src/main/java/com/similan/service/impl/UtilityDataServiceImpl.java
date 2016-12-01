package com.similan.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.similan.domain.entity.util.SlideShowImage;
import com.similan.domain.repository.util.SlideShowImageRepository;
import com.similan.framework.dto.Image;
import com.similan.service.api.UtilityDataService;

/**
 * 
 * @author supapal
 *
 */
public class UtilityDataServiceImpl implements UtilityDataService {
	
	@Autowired
	private SlideShowImageRepository imageRepository;

	/**
	 * get the images
	 */
	public List<Image> getHomePageSlideshowImages() {
		
		List<SlideShowImage> slideShowImageList = imageRepository.findAll();
		List<Image> retImageList = new ArrayList<Image>();
		
		for(SlideShowImage slideImage : slideShowImageList){
			Image image = new Image(slideImage.getImageInfo());
			retImageList.add(image);
		}
		
		return retImageList;
	}

}
