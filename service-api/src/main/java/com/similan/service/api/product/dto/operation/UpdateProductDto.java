package com.similan.service.api.product.dto.operation;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.base.dto.operation.OperationDto;

public class UpdateProductDto extends OperationDto {
	
	@XmlAttribute
	private String description;
	
	@XmlAttribute
	protected String imageLocation;
	
	public UpdateProductDto(String description, String imageLocation){
		this.description = description;
		this.imageLocation = imageLocation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageLocation() {
		return imageLocation;
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}
	
	

}
