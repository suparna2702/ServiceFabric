package com.similan.framework.dto;

import java.io.Serializable;

import com.similan.domain.entity.util.SlideShowImageInfo;

public class Image implements Serializable {

	private static final long serialVersionUID = 4130708609677572007L;
    
    private SlideShowImageInfo slideShowImage = new SlideShowImageInfo();
    
	public Image(String url, String caption) {
		
		if(this.slideShowImage != null){
			slideShowImage = new SlideShowImageInfo();
		}
		
		this.slideShowImage.setUrl(url);
		this.slideShowImage.setCaption(caption);
	}
	
	public Image(SlideShowImageInfo imageInfo){
		this.slideShowImage = imageInfo;
	}
	
	public SlideShowImageInfo getSlideShowImage() {
		return slideShowImage;
	}

	public void setSlideShowImage(SlideShowImageInfo slideShowImage) {
		this.slideShowImage = slideShowImage;
	}

	public String getTitle() {
		return this.slideShowImage.getTitle();
	}

	public void setTitle(String title) {
		this.slideShowImage.setTitle(title);
	}

	public int getHeight() {
		return this.slideShowImage.getHeight();
	}

	public void setHeight(int height) {
		this.slideShowImage.setHeight(height);
	}

	public int getWidth() {
		return this.slideShowImage.getWidth();
	}

	public void setWidth(int width) {
		this.slideShowImage.setWidth(width);
	}

	public String getUrl() {
		return this.slideShowImage.getUrl();
	}

	public void setUrl(String url) {
		this.slideShowImage.setUrl(url);
	}

	public String getCaption() {
		return this.slideShowImage.getCaption();
	}

	public void setCaption(String caption) {
		this.slideShowImage.setCaption(caption);
	}

}
