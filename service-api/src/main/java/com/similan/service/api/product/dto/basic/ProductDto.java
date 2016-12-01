package com.similan.service.api.product.dto.basic;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.product.ProductNodeType;
import com.similan.service.api.product.dto.key.ProductKey;

public class ProductDto extends KeyHolderDto<ProductKey> {

  @XmlAttribute
  protected String description;

  @XmlAttribute
  protected String productNodeCode;

  @XmlAttribute
  protected String imageLocation;

  @XmlAttribute
  protected String domainId = UUID.randomUUID().toString();

  @XmlAttribute
  private ProductNodeType productNodeType;

  protected ProductDto() {
  }

  public ProductDto(ProductKey key, String description, String productNodeCode,
      String imageLocation, ProductNodeType productNodeType, String domainId) {
    super(key);
    this.description = description;
    this.imageLocation = imageLocation;
    this.productNodeType = productNodeType;
    this.productNodeCode = productNodeCode;
    this.domainId = domainId;
  }

  public String getDomainId() {
    return domainId;
  }

  public String getDescription() {
    return description;
  }

  public String getProductNodeCode() {
    return productNodeCode;
  }

  public String getImageLocation() {
    return imageLocation;
  }

  public ProductNodeType getProductNodeType() {
    return productNodeType;
  }

  @Override
  public String toString() {
    return "ProductDto [description=" + description + ", productNodeCode="
        + productNodeCode + ", imageLocation=" + imageLocation
        + ", productNodeType=" + productNodeType + "]";
  }

}
