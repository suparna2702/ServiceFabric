package com.similan.service.api.product.dto.operation;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.base.dto.operation.OperationDto;
import com.similan.service.api.product.ProductNodeType;

public class NewProductDto extends OperationDto {

  @XmlAttribute
  private String description;

  @XmlAttribute
  private String productNodeCode;

  @XmlAttribute
  private ProductNodeType productType;

  @XmlAttribute
  private String domainId;

  public NewProductDto() {
  }

  public NewProductDto(String description, String productNodeCode,
      ProductNodeType productType, String domainId) {
    this.description = description;
    this.productNodeCode = productNodeCode;
    this.productType = productType;
    this.domainId = domainId;
  }

  public ProductNodeType getProductType() {
    return productType;
  }

  public String getDescription() {
    return description;
  }

  public String getProductNodeCode() {
    return productNodeCode;
  }

}
