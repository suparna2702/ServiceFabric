package com.similan.portal.databean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.similan.framework.dto.Image;
import com.similan.portal.view.BaseView;

@ManagedBean(name = "utilityDataBean")
@ApplicationScoped
public class UtilityDateBean extends BaseView {
	private static final long serialVersionUID = 1L;
	
	private List<Image> homeSlideshowImages = null;
	
	@PostConstruct
	public void init() {
		
		this.homeSlideshowImages = this.platformMetadataService.getHomePageSlideshowImages();

	}
	
    public List<Image> getHomeSlideshowImages() {
		return homeSlideshowImages;
	}

	public void setHomeSlideshowImages(List<Image> homeSlideshowImages) {
		this.homeSlideshowImages = homeSlideshowImages;
	}
}
