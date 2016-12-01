package com.similan.service.api.product.dto.basic;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.product.dto.key.ProductFeatureKey;

public class ProductFeatureDto extends KeyHolderDto<ProductFeatureKey> {
	
  @XmlAttribute
  private String name; 

  @XmlAttribute
  private String description;

  protected ProductFeatureDto() {
  }

  public ProductFeatureDto(ProductFeatureKey key, String name, 
		  String description) {
    super(key);
    this.name = name;
    this.description = description;
  }

  public String getName() {
	  return name;
  }

  public String getDescription() {
     return description;
  }

}
