package com.similan.framework.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.similan.domain.entity.util.SlideShowImageInfo;

@XmlRootElement(name = "slideShowImageInfoList")
public class SlideShowImageListDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<SlideShowImageInfo> imageList;

	@XmlElements(@XmlElement(name="slideShowImageInfo", 
			     type=SlideShowImageInfo.class))
	public List<SlideShowImageInfo> getImageList() {
		return imageList;
	}

	public void setImageList(List<SlideShowImageInfo> imageList) {
		this.imageList = imageList;
	}
}
