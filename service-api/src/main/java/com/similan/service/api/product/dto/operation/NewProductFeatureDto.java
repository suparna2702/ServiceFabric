package com.similan.service.api.product.dto.operation;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.base.dto.operation.OperationDto;

public class NewProductFeatureDto extends OperationDto {
  
  @XmlAttribute
  private String name;
	
  @XmlAttribute
  private String description;

  public NewProductFeatureDto() {
  }

  public NewProductFeatureDto(String name, String description) {
    this.description = description;
    this.name = name;
  }
 
  public String getName() {
	return name;
  }

  public String getDescription() {
    return description;
  }

}
