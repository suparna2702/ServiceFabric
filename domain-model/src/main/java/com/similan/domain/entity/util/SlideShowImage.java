package com.similan.domain.entity.util;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="slideShowImage")
public class SlideShowImage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Embedded
	private SlideShowImageInfo imageInfo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SlideShowImageInfo getImageInfo() {
		return imageInfo;
	}

	public void setImageInfo(SlideShowImageInfo imageInfo) {
		this.imageInfo = imageInfo;
	}
}
